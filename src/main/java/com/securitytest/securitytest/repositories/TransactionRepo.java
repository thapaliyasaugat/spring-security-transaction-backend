package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions,Integer> {
    @Query(value = "select * from transactions where transaction_from=:id",nativeQuery = true)
    Page<Transactions> ownSendTransactions(int id,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_from=:id) where created_at between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    Page<Transactions> ownSendTransactionsBetweenDate(int id,Date transactionTimeStart,Date transactionTimeEnd,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_to=:id) where created_at between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    Page<Transactions> ownReceivedTransactionsBetweenDate(int id,Date transactionTimeStart,Date transactionTimeEnd,Pageable pageable);
    @Query(value = "select * from transactions where transaction_to = :id",nativeQuery = true)
    Page<Transactions> ownReceivedTransactions(int id,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_from=:id) where amount between :fromAmount and :toAmount",nativeQuery = true)
    Page<Transactions> ownSendTransactionBetweenAmount(int id,Double fromAmount,Double toAmount,Pageable p);
    @Query(value = "select * from (select * from transactions where transaction_to=:id) where amount between :fromAmount and :toAmount",nativeQuery = true)
    Page<Transactions> ownReceivedTransactionBetweenAmount(int id,Double fromAmount,Double toAmount,Pageable p);
    @Query(value = "select * from (select * from transactions where transaction_from=:id or transaction_to=:id) where amount between :fromAmount and :toAmount",nativeQuery = true)
    Page<Transactions> ownTransactionBetweenAmount(int id,Double fromAmount,Double toAmount,Pageable p);
    Page<Transactions> findByCode(String code,Pageable pageable);
    @Query(value = "select * from transactions where created_at between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    Page<Transactions> findAllByTransactionTimeBetween(
            Date transactionTimeStart,
            Date transactionTimeEnd, Pageable pageable);
    @Query(value = "select * from transactions where transaction_from=:id or transaction_to=:id",nativeQuery = true)
    Page<Transactions> ownTransactions(int id,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_from=:id OR transaction_to = :id) where created_at between :transactionTimeStart and :transactionTimeEnd",nativeQuery = true)
    Page<Transactions> myTransactionsByInterval(Date transactionTimeStart,Date transactionTimeEnd, int id ,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_from=:id OR transaction_to = :id) where created_at between :transactionTimeStart and :transactionTimeEnd HAVING amount between :fromAmount and :toAmount ",nativeQuery = true)
    Page<Transactions> myTransactionsByDateAndAmount(Date transactionTimeStart,Date transactionTimeEnd,Double fromAmount,Double toAmount, int id ,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_from=:id) where created_at between :transactionTimeStart and :transactionTimeEnd HAVING amount between :fromAmount and :toAmount ",nativeQuery = true)
    Page<Transactions> mySendTransactionsByDateAndAmount(Date transactionTimeStart,Date transactionTimeEnd,Double fromAmount,Double toAmount, int id ,Pageable pageable);
    @Query(value = "select * from (select * from transactions where transaction_to = :id) where created_at between :transactionTimeStart and :transactionTimeEnd HAVING amount between :fromAmount and :toAmount ",nativeQuery = true)
    Page<Transactions> myReceivedTransactionsByDateAndAmount(Date transactionTimeStart,Date transactionTimeEnd,Double fromAmount,Double toAmount, int id ,Pageable pageable);

}
