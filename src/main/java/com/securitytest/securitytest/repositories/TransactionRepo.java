package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions,Integer> {
    Transactions findByCode(String code);
    @Query(value = "select * from transactions where created_date between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    List<Transactions> findAllByTransactionTimeBetween(
            Date transactionTimeStart,
            Date transactionTimeEnd);
    @Query(value = "select * from transactions where transaction_from=:id or transaction_to=:id",nativeQuery = true)
    List<Transactions> ownTransactions(int id);
}
