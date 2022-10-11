package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.*;

public interface TransactionService {
    ApiResponse<?> makeTransaction(TransactionRequest transactionRequest);
//    ApiResponse<PageableResponse> transactionsByCode(TransactionByCode transactionByCode);
    ApiResponse<PageableResponse> allTransactions(TransactionPageRequest transactionPageRequest,String email);
//    ApiResponse<PageableResponse> transactionByInterval(String fromDate, String toDate, PageRequestObj pageRequest);
    ApiResponse<PageableResponse> ownTransactions(TransactionPageRequest transactionPageRequest);
}
