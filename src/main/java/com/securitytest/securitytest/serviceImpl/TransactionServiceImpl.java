package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.service.TransactionService;
import com.securitytest.securitytest.service.UserService;
import com.securitytest.securitytest.util.DateValidator;
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
    public ApiResponse makeTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto fromUser = userService.userByEmail(authentication.getName());
        UserDto toUser = userService.userByEmail(transactionRequest.getToUser());
        checkTransactionValidity(transactionRequest,fromUser,toUser);
        Transactions transactions = new Transactions();
        transactions.setAmount(transactionRequest.getAmount());
        transactions.setCustomer_from(modelMapper.map(fromUser,User.class));
        transactions.setCustomer_to(modelMapper.map(toUser, User.class));
        updateBalance(transactionRequest,fromUser,toUser);
        transactionRepo.save(transactions);
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(0);
        response.setMessage("transaction successful.");
        return response;
    }
    private void checkTransactionValidity(TransactionRequest transactionRequest,UserDto fromUser,UserDto toUser){
        if(fromUser.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("Can't perform transaction. Your account is blocked.");
        if(toUser.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("Can't perform transaction. The account you are trying to send Money is currently blocked.");
        if(fromUser.getId() == toUser.getId()) throw new RuntimeException("You can't make transaction to yourself");
        if(transactionRequest.getAmount()>50000) throw new RuntimeException("Can't make transaction above 50,000.");
        if(transactionRequest.getAmount()<500) throw new RuntimeException("Can't make transaction below 500. ");
        if(fromUser.getBalance()<transactionRequest.getAmount()) throw new RuntimeException("You don't have enough balance.");
    }

    private void updateBalance(TransactionRequest transactionRequest, UserDto fromUser, UserDto toUser){
        try {
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            userService.updateUser(fromUser);
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            userService.updateUser(toUser);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong, Can't perform transaction.");
        }
    }

    @Override
    public ApiResponse<PageableResponse> transactionsByCode(TransactionByCode transactionByCode) {
        Pageable p = org.springframework.data.domain.PageRequest.of(0, 8);
        Page<Transactions> transactions = transactionRepo.findByCode(transactionByCode.getCode(), p);
        return getTransactionPageableResponse(transactions);
    }


    @Override
    @Cacheable()
    public ApiResponse<PageableResponse> allTransactions(PageRequestObj pageRequest) {
        log.info("Request received for all transaction.");
        Pageable p = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),Sort.by("createdAt").descending());
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
            DateValidator.ValidateDateRange(from,to);
            Pageable p = PageRequest.of(pageRequest.getPageNumber(),pageRequest.getPageSize(),Sort.by("created_at").descending());
            Page<Transactions> transactionsPage = transactionRepo.findAllByTransactionTimeBetween(from, to,p);
            return getTransactionPageableResponse(transactionsPage);
        }catch (Exception e){
            throw new RuntimeException("Date type incompatible");
        }
    }

    @Override
    public ApiResponse<PageableResponse> ownTransactions(TransactionPageRequest transactionPageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.userByEmail(authentication.getName());
        Pageable p = PageRequest.of(transactionPageRequest.getPageNumber(),transactionPageRequest.getPageSize(), Sort.by("created_at").descending());
        Page<Transactions> ownTransactionList = ownTransactionByFilterOnly(transactionPageRequest.getFilter(),user,p);
        return getTransactionPageableResponse(ownTransactionList);

    }
    private Page<Transactions> ownTransactionByFilterOnly(String filter, UserDto user, Pageable p){
        Page<Transactions> ownTransactionList = Page.empty();
        if(filter.equals("SEND") || filter.equals("RECEIVED")) {
            if (filter.equals("SEND")) ownTransactionList = transactionRepo.ownSendTransactions(user.getId(), p);
            if (filter.equals("RECEIVED")) ownTransactionList = transactionRepo.ownReceivedTransactions(user.getId(), p);
        }else{
            ownTransactionList = transactionRepo.ownTransactions(user.getId(), p);
        }
        return ownTransactionList;
    }
    private Page<Transactions> ownTransactionByFilterAndDate(String filter,String fromDate,String toDate, UserDto user, Pageable p){
        try {
            Page<Transactions> ownTransactionList = Page.empty();
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            DateValidator.ValidateDateRange(from, to);
            if (filter.equals("SEND") || filter.equals("RECEIVED")) {
                if (filter.equals("SEND"))
                    ownTransactionList = transactionRepo.ownSendTransactionsBetweenDate(user.getId(), from, to, p);
                if (filter.equals("RECEIVED"))
                    ownTransactionList = transactionRepo.ownReceivedTransactionsBetweenDate(user.getId(), from, to, p);
            } else {
                ownTransactionList = transactionRepo.myTransactionsByInterval(from, to, user.getId(), p);
            }
            return ownTransactionList;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Error Parsing Date.");
        }
    }
    private Page<Transactions> ownTransactionByFilterAndAmount(Double fromAmount,Double toAmount,String filter ,UserDto user, Pageable p){
        try {
            Page<Transactions> ownTransactionList = Page.empty();
            if (filter.equals("SEND") || filter.equals("RECEIVED")) {
                if (filter.equals("SEND"))
                    ownTransactionList = transactionRepo.ownSendTransactionBetweenAmount(user.getId(), fromAmount,toAmount,p);
                if (filter.equals("RECEIVED"))
                    ownTransactionList = transactionRepo.ownReceivedTransactionBetweenAmount(user.getId(), fromAmount,toAmount,p);
            } else {
                ownTransactionList = transactionRepo.myTransactionsByInterval(from, to, user.getId(), p);
            }
            return ownTransactionList;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Error Parsing Date.");
        }
    }

    @Override
    public ApiResponse<PageableResponse> myTransactionByInterval(String fromDate, String toDate, PageRequestObj pageRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userService.userByEmail(email);
        try {
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            if(!from.before(to)) throw new RuntimeException("invalid Date interval");
            Pageable p = PageRequest.of(pageRequest.getPageNumber(),pageRequest.getPageSize(),Sort.by("created_at").descending());
            Page<Transactions> transactionsPage = transactionRepo.myTransactionsByInterval(from, to,user.getId(),p);
            return getTransactionPageableResponse(transactionsPage);
        }catch (Exception e){
            log.info("Error fetching individual user transactions by interval , {}",e.getMessage());
            throw new RuntimeException("Date type incompatible");
        }
    }
}
