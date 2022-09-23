package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.*;

public interface TransactionService {
    ApiResponse makeTransaction(TransactionRequest transactionRequest);
    PageableResponse transactionsByCode(TransactionByCode transactionByCode);
    PageableResponse allTransactions(PageRequestObj pageRequest);
    PageableResponse transactionByInterval(String fromDate, String toDate, PageRequestObj pageRequest);
    PageableResponse ownTransactions(PageRequestObj pageRequest, String filter);

}
