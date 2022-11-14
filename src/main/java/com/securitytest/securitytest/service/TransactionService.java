package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.*;

public interface TransactionService {
    ApiResponse<?> makeTransaction(TransactionRequest transactionRequest);
    ApiResponse<PageableResponse> allTransactions(TransactionPageRequest transactionPageRequest,String email);
    ApiResponse<PageableResponse> ownTransactions(TransactionPageRequest transactionPageRequest);
}
