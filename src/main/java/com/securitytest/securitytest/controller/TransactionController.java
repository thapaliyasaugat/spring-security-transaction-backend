package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.TransactionDto;
import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        ApiResponse response = transactionService.makeTransaction(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> transactionByCode(@PathVariable String code) {
        TransactionDto transactionDto = transactionService.transactionsByCode(code);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> allTransactions() {
        List<TransactionDto> transactionDtos = transactionService.allTransactions();
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @GetMapping("/time/between")
    public ResponseEntity<?> transactionByInterval(@DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        log.info("from date : {}",fromDate);
        log.info("to date : {}",toDate);
        List<TransactionDto> transactionDtos = transactionService.transactionByInterval(fromDate,toDate);
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @GetMapping("/my_transactions")
    public ResponseEntity<?> myTransactions() {
        List<TransactionDto> transactions = transactionService.ownTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
