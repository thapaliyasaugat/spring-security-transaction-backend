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

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Transactional
    public ApiResponse makeTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User fromUser = userRepo.findByEmail(authentication.getName());
        User toUser = userRepo.findById(transactionRequest.getToUser()).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(transactionRequest.getToUser())));
        checkTransactionValidity(transactionRequest,fromUser,toUser);
        Transactions transactions = new Transactions();
        transactions.setAmount(transactionRequest.getAmount());
        transactions.setCustomer_from(fromUser);
        transactions.setCustomer_to(toUser);
        updateBalance(transactionRequest,fromUser,toUser);
        transactionRepo.save(transactions);
        ApiResponse response = new ApiResponse();
        response.setStatus(0);
        response.setMessage("transaction successful.");
        return response;
    }
    private void checkTransactionValidity(TransactionRequest transactionRequest,User fromUser,User toUser){
        if(fromUser.getStatus().equals(UserStatus.BLOCKED.status)) throw new RuntimeException("Can't perform transaction. Your account is blocked.");
        if(toUser.getStatus().equals(UserStatus.BLOCKED.status)) throw new RuntimeException("Can't perform transaction. The account you are trying to send Money is currently blocked.");
        if(fromUser.getId() == toUser.getId()) throw new RuntimeException("You can't make transaction to yourself");
        if(transactionRequest.getAmount()>50000) throw new RuntimeException("Can't make transaction above 50,000.");
        if(transactionRequest.getAmount()<500) throw new RuntimeException("Can't make transaction below 500. ");
        if(fromUser.getBalance()<transactionRequest.getAmount()) throw new RuntimeException("You don't have enough balance.");
    }

    private void updateBalance(TransactionRequest transactionRequest, User fromUser, User toUser){
        try {
            fromUser.setBalance(fromUser.getBalance() - transactionRequest.getAmount());
            userRepo.save(fromUser);
            toUser.setBalance(toUser.getBalance() + transactionRequest.getAmount());
            userRepo.save(toUser);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong, Can't perform transaction.");
        }
    }

    @Override
    public List<TransactionDto> transactionsByCode(String id) {
        Transactions transactions = transactionRepo.findByCode(id);
        List<TransactionDto> list = new ArrayList<>();
        list.add(modelMapper.map(transactions,TransactionDto.class));
        return list;
    }

    @Override
    public List<TransactionDto> allTransactions() {
        List<Transactions> listOfTransactions = transactionRepo.findAll();
        return listOfTransactions.stream().map(transaction->modelMapper.map(transaction,TransactionDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> transactionByInterval(String fromDate , String toDate) {
        try {
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            if(!from.before(to)) throw new RuntimeException("invalid Date interval");
            List<Transactions> transactions = transactionRepo.findAllByTransactionTimeBetween(from, to);
            return transactions.stream().map(transaction->modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());

        }catch (Exception e){
            throw new RuntimeException("Date type incompatible");
        }
    }

    @Override
    public List<TransactionDto> ownTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        List<Transactions> ownTransactionList = transactionRepo.ownTransactions(user.getId());
        return ownTransactionList.stream().map(transaction->modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
    }
}
