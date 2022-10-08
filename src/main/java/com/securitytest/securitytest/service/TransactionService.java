package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.*;

public interface TransactionService {
    ApiResponse makeTransaction(TransactionRequest transactionRequest);
    ApiResponse<PageableResponse> transactionsByCode(TransactionByCode transactionByCode);
    ApiResponse<PageableResponse> allTransactions(PageRequestObj pageRequest);
    ApiResponse<PageableResponse> transactionByInterval(String fromDate, String toDate, PageRequestObj pageRequest);
    ApiResponse<PageableResponse> ownTransactions(TransactionPageRequest transactionPageRequest);
    ApiResponse<PageableResponse> myTransactionByInterval(String fromDate, String toDate, PageRequestObj pageRequest);


}
