package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface TransactionRepo extends JpaRepository<Transactions,Integer> {
    @Query(value = "select * from transactions where transaction_from=:id",nativeQuery = true)
    Page<Transactions> ownSendTransactions(int id,Pageable pageable);

    @Query(value = "select * from transactions where transaction_to = :id",nativeQuery = true)
    Page<Transactions> ownReceivedTransactions(int id,Pageable pageable);
    Page<Transactions> findByCode(String code,Pageable pageable);
    @Query(value = "select * from transactions where created_at between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    Page<Transactions> findAllByTransactionTimeBetween(
            Date transactionTimeStart,
            Date transactionTimeEnd, Pageable pageable);
    @Query(value = "select * from transactions where transaction_from=:id or transaction_to=:id",nativeQuery = true)
    Page<Transactions> ownTransactions(int id,Pageable pageable);
}
