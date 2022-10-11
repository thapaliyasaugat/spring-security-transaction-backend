package com.securitytest.securitytest.dao;

import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.resource.TransactionPageRequest;
import com.securitytest.securitytest.resource.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionDao {
    Page<Transactions> allTransactions(TransactionPageRequest transactionPageRequest, UserDto user, Pageable pageable);
}
