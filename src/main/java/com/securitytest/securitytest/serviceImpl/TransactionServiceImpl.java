package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.dao.TransactionDao;
import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.service.TransactionService;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//@CacheConfig(cacheNames = {"transaction"})
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TransactionDao transactionDao;


    public TransactionServiceImpl(TransactionRepo transactionRepo, UserService userService, ModelMapper modelMapper, TransactionDao transactionDao) {
        this.transactionRepo = transactionRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.transactionDao = transactionDao;
    }

    @Override
    @Transactional
    public ApiResponse<?> makeTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto fromUser = userService.userByEmail(authentication.getName());
        UserDto toUser = userService.userByEmail(transactionRequest.getToUser());
        checkTransactionValidity(transactionRequest, fromUser, toUser);
        Transactions transactions = new Transactions();
        transactions.setAmount(transactionRequest.getAmount());
        transactions.setCustomer_from(modelMapper.map(fromUser, User.class));
        transactions.setCustomer_to(modelMapper.map(toUser, User.class));
        updateBalance(transactionRequest, fromUser, toUser);
        transactionRepo.save(transactions);
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(0);
        response.setMessage("transaction successful.");
        return response;
    }

    private void checkTransactionValidity(TransactionRequest transactionRequest, UserDto fromUser, UserDto toUser) {
        if (fromUser.getStatus().equals(UserStatus.BLOCKED))
            throw new RuntimeException("Can't perform transaction. Your account is blocked.");
        if (toUser.getStatus().equals(UserStatus.BLOCKED))
            throw new RuntimeException("Can't perform transaction. The account you are trying to send Money is currently blocked.");
        if (fromUser.getId() == toUser.getId()) throw new RuntimeException("You can't make transaction to yourself");
        if (transactionRequest.getAmount() > 50000) throw new RuntimeException("Can't make transaction above 50,000.");
        if (transactionRequest.getAmount() < 500) throw new RuntimeException("Can't make transaction below 500. ");
        if (fromUser.getBalance() < transactionRequest.getAmount())
            throw new RuntimeException("You don't have enough balance.");
    }

    private void updateBalance(TransactionRequest transactionRequest, UserDto fromUser, UserDto toUser) {
        try {
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            userService.updateUser(fromUser);
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            userService.updateUser(toUser);
        } catch (Exception e) {
            throw new RuntimeException("Something goes wrong, Can't perform transaction.");
        }
    }
    @Override
//    @Cacheable()
    public ApiResponse<PageableResponse> allTransactions(TransactionPageRequest transactionPageRequest,String email) {
        log.info("Request received for all transaction.");
        UserDto userDto=null;
        if(!email.isEmpty()) {
            userDto = userService.userByEmail(email);
            if (userDto == null) throw new ResourceNotFoundException("user", "email", email);
        }
        Pageable p = org.springframework.data.domain.PageRequest.of(transactionPageRequest.getPageNumber(), transactionPageRequest.getPageSize(), Sort.by("createdAt").descending());
        Page<Transactions> listOfTransactions = transactionDao.allTransactions(transactionPageRequest,userDto,p);
        return getTransactionPageableResponse(listOfTransactions);
    }

    private ApiResponse<PageableResponse> getTransactionPageableResponse(Page<Transactions> listOfTransactions) {
        List<TransactionDto> transactionDtos = listOfTransactions.getContent().stream().map(t -> modelMapper.map(t, TransactionDto.class)).collect(Collectors.toList());
        PageableResponse response = new PageableResponse();
        response.setContent(transactionDtos);
        response.setPageNumber(listOfTransactions.getNumber());
        response.setPageSize(listOfTransactions.getSize());
        response.setTotalNoOfPages(listOfTransactions.getTotalPages());
        response.setTotalNoOfElements(listOfTransactions.getTotalElements());
        return new ApiResponse<>(response, "pageable response", 0);
    }
    @Override
    public ApiResponse<PageableResponse> ownTransactions(TransactionPageRequest transactionPageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Request to view all own transactions by {}",authentication.getName());
        UserDto user = userService.userByEmail(authentication.getName());
        Pageable p = PageRequest.of(transactionPageRequest.getPageNumber(), transactionPageRequest.getPageSize(), Sort.by("created_at").descending());
        Page<Transactions> transactionsList = transactionDao.allTransactions(transactionPageRequest,user,p);
        return getTransactionPageableResponse(transactionsList);
    }
}
