package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.TransactionDto;
import com.securitytest.securitytest.resource.TransactionRequest;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    ApiResponse makeTransaction(TransactionRequest transactionRequest);
    TransactionDto transactionsByCode(String id);
    List<TransactionDto> allTransactions();
    List<TransactionDto> transactionByInterval(Date fromDate, Date toDate);
    List<TransactionDto> ownTransactions();

}
