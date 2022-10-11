package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        ApiResponse<?> response = transactionService.makeTransaction(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/code")
//    public ResponseEntity<?> transactionByCode(@RequestBody TransactionByCode transactionByCode) {
//        ApiResponse<PageableResponse> transactionDto = transactionService.transactionsByCode(transactionByCode);
//        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
//    }

    @PostMapping("/list")
    public ResponseEntity<?> allTransactions(@RequestBody TransactionPageRequest transactionPageRequest,
                                             @RequestParam(defaultValue = "") String email) {
        ApiResponse<PageableResponse> transactionDtos = transactionService.allTransactions(transactionPageRequest,email);
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

//    @PostMapping("/time/between")
//    public ResponseEntity<?> transactionByInterval(@DateTimeFormat(pattern = "yyyy-MM-dd") String fromDate,
//                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") String toDate,
//                                                   @RequestBody PageRequestObj pageRequest) {
//        log.info("from date : {}",fromDate);
//        log.info("to date : {}",toDate);
//        ApiResponse<PageableResponse> transactionDtos = transactionService.transactionByInterval(fromDate,toDate,pageRequest);
//        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
//    }

    @PostMapping("/my_transactions")
    public ResponseEntity<?> myTransactions(@RequestBody TransactionPageRequest transactionPageRequest){
        ApiResponse<PageableResponse> transactions = transactionService.ownTransactions(transactionPageRequest);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
