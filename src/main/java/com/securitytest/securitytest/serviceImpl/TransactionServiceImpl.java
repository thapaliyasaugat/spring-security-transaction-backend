package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.service.TransactionChain;
import com.securitytest.securitytest.service.TransactionService;
import com.securitytest.securitytest.service.UserService;
import com.securitytest.securitytest.serviceImpl.transaction_chain_impl.CheckTransactionValidity;
import com.securitytest.securitytest.serviceImpl.transaction_chain_impl.PerformCashback;
import com.securitytest.securitytest.serviceImpl.transaction_chain_impl.UpdateTransactionBalance;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = {"transaction"})
public class TransactionServiceImpl implements TransactionService {
private final TransactionRepo transactionRepo;
private final UserService userService;
private final ModelMapper modelMapper;


    public TransactionServiceImpl(TransactionRepo transactionRepo, UserService userService, ModelMapper modelMapper) {
        this.transactionRepo = transactionRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ApiResponse<?> makeTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto fromUser = userService.userByEmail(authentication.getName());
        UserDto toUser = userService.userByEmail(transactionRequest.getToUser());
        TransactionChain transactionCheckValidity = new CheckTransactionValidity();
        TransactionChain updateTransactionBalance = new UpdateTransactionBalance();
        TransactionChain transactionCashback = new PerformCashback();

        transactionCheckValidity.setNextChain(updateTransactionBalance);
        updateTransactionBalance.setNextChain(transactionCashback);

        transactionCheckValidity.settlement(transactionRequest,fromUser,toUser);

        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(0);
        response.setMessage("transaction successful.");
        return response;
    }
    @Override
    @Cacheable
    public ApiResponse<PageableResponse> transactionsByCode(TransactionByCode transactionByCode) {
        Pageable p = PageRequest.of(0, 8);
        Page<Transactions> transactions = transactionRepo.findByCode(transactionByCode.getCode(), p);
        return getTransactionPageableResponse(transactions);
    }


    @Override
    @Cacheable()
    public ApiResponse<PageableResponse> allTransactions(PageRequestObj pageRequest) {
        log.info("Request received for all transaction.");
        Pageable p = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),Sort.by("createdAt").descending());
        Page<Transactions> listOfTransactions = transactionRepo.findAll(p);
        return getTransactionPageableResponse(listOfTransactions);
    }

    private ApiResponse<PageableResponse> getTransactionPageableResponse(Page<Transactions> listOfTransactions) {
        List<TransactionDto> transactionDtos = listOfTransactions.getContent().stream().map(t->modelMapper.map(t, TransactionDto.class)).collect(Collectors.toList());
        PageableResponse response = new PageableResponse();
        response.setContent(transactionDtos);
        response.setPageNumber(listOfTransactions.getNumber());
        response.setPageSize(listOfTransactions.getSize());
        response.setTotalNoOfPages(listOfTransactions.getTotalPages());
        response.setTotalNoOfElements(listOfTransactions.getTotalElements());
        return new ApiResponse<>(response,"pageable response",0);
    }

    @Override
    public ApiResponse<PageableResponse> transactionByInterval(String fromDate , String toDate , PageRequestObj pageRequest) {
        try {
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            if(!from.before(to)) throw new RuntimeException("invalid Date interval");
            Pageable p = PageRequest.of(pageRequest.getPageNumber(),pageRequest.getPageSize(),Sort.by("created_at").descending());
            Page<Transactions> transactionsPage = transactionRepo.findAllByTransactionTimeBetween(from, to,p);
            return getTransactionPageableResponse(transactionsPage);
        }catch (Exception e){
            throw new RuntimeException("Date type incompatible");
        }
    }

    @Override
    public ApiResponse<PageableResponse> ownTransactions(PageRequestObj pageRequest, String filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.userByEmail(authentication.getName());
        Pageable p = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),pageRequest.getPageSize(), Sort.by("created_at").descending());
        Page<Transactions> ownTransactionList = Page.empty();
        if(filter.equals("SEND") || filter.equals("RECEIVED")) {
            if (filter.equals("SEND")) ownTransactionList = transactionRepo.ownSendTransactions(user.getId(), p);
            if (filter.equals("RECEIVED")) ownTransactionList = transactionRepo.ownReceivedTransactions(user.getId(), p);
        }else{
            ownTransactionList = transactionRepo.ownTransactions(user.getId(), p);
        }
        return getTransactionPageableResponse(ownTransactionList);

    }
}
