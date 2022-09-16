package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
private final TransactionRepo transactionRepo;
private final UserRepo userRepo;
private final ModelMapper modelMapper;


    public TransactionServiceImpl(TransactionRepo transactionRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse makeTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User fromUser = userRepo.findByEmail(authentication.getName());
        if(fromUser.getStatus().equals(UserStatus.BLOCKED.status)) throw new RuntimeException("Can't perform transaction. Your account is blocked.");
        User toUser = userRepo.findById(transactionRequest.getToUser()).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(transactionRequest.getToUser())));
        if(toUser.getStatus().equals(UserStatus.BLOCKED.status)) throw new RuntimeException("Can't perform transaction. The account you are trying to send Money is currently blocked.");
        if(fromUser.getId() == toUser.getId()) throw new RuntimeException("You can't make transaction to yourself");
        if(transactionRequest.getAmount()>50000) throw new RuntimeException("Can't make transaction above 50,000.");
        if(transactionRequest.getAmount()<500) throw new RuntimeException("Can't make transaction below 500. ");
        Transactions transactions = new Transactions();
        transactions.setAmount(transactionRequest.getAmount());
        transactions.setCustomer_from(fromUser);
        transactions.setCustomer_to(toUser);
        transactionRepo.save(transactions);
        ApiResponse response = new ApiResponse();
        response.setStatus(0);
        response.setMessage("transaction successful.");
        return response;
    }

    @Override
    public TransactionDto transactionsByCode(String id) {
        Transactions transactions = transactionRepo.findByCode(id);
        return modelMapper.map(transactions,TransactionDto.class);
    }

    @Override
    public List<TransactionDto> allTransactions() {
        List<Transactions> listOfTransactions = transactionRepo.findAll();
        return listOfTransactions.stream().map(transaction->modelMapper.map(transaction,TransactionDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> transactionByInterval(Date fromDate , Date toDate) {
        List<Transactions> transactions = transactionRepo.findAllByTransactionTimeBetween(fromDate, toDate);
        return transactions.stream().map(transaction->modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> ownTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        List<Transactions> ownTransactionList = transactionRepo.ownTransactions(user.getId());
        return ownTransactionList.stream().map(transaction->modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
    }
}
