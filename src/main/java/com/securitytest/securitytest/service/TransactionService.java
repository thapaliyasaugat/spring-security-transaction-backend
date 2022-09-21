package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.*;

import java.util.List;

public interface TransactionService {
    ApiResponse makeTransaction(TransactionRequest transactionRequest);
    PageableResponse transactionsByCode(TransactionByCode transactionByCode);
    PageableResponse allTransactions(PageRequest pageRequest);
    PageableResponse transactionByInterval(String fromDate, String toDate, PageRequest pageRequest);
    PageableResponse ownTransactions(PageRequest pageRequest,String filter);

}
