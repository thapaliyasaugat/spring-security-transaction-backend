package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepo transactionRepo;

    @Spy
    private ModelMapper modelMapper;
    private User fromUser;
    private User toUser;
    private Role role;
    private Transactions sampleTransaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        role = new Role(2, RoleName.CUSTOMER,null);
        fromUser = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        toUser = User.builder().id(1).userName("Sara").email("sara@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        sampleTransaction = Transactions.builder().id(1).code("fakeCode").amount(10000.00)
                .customer_from(fromUser).customer_to(toUser).build();
    }

    @Test
    void makeTransaction() {
        UserDto fromUserDto=modelMapper.map(fromUser, UserDto.class);
        TransactionRequest transactionRequest = TransactionRequest.builder().toUser("saugat@email.com")
                .amount(3453.09).build();
        when(userService.userByEmail(anyString())).thenReturn(fromUserDto);
    }

    @Test
    void transactionsByCode() {
        TransactionByCode transactionByCode = TransactionByCode.builder().code("fakeCode1").build();
        Page<Transactions> transactions = new PageImpl<>(Arrays.asList(sampleTransaction));
        when(transactionRepo.findByCode(anyString(), any(Pageable.class))).thenReturn(transactions);
        ApiResponse<PageableResponse> response = transactionServiceImpl.transactionsByCode(transactionByCode);
        assertEquals(response.getData().getPageSize(),1);
        assertEquals(response.getData().getPageNumber(),0);
        assertEquals(response.getData().getTotalNoOfPages(),1);
        assertEquals(response.getData().getTotalNoOfElements(),1);
        assertNotNull(response.getData());

    }

    @Test
    void allTransactions() {
        Transactions firstTransaction = Transactions.builder().id(1).code("fakeCode").amount(10000.00)
                .customer_from(fromUser).customer_to(toUser).build();
        Transactions secondTransaction = Transactions.builder().id(1).code("fakeCode2").amount(20000.00)
                .customer_from(toUser).customer_to(fromUser).build();
        Page<Transactions> transactions = new PageImpl<>(Arrays.asList(sampleTransaction,firstTransaction,secondTransaction));
        when(transactionRepo.findAll(any(Pageable.class))).thenReturn(transactions);
        PageRequestObj pageRequest = PageRequestObj.builder().pageNumber(0).pageSize(3).build();
        ApiResponse<PageableResponse> response = transactionServiceImpl.allTransactions(pageRequest);
        assertEquals(response.getData().getPageNumber(),0);
        assertEquals(response.getData().getPageSize(),3);
        assertEquals(response.getData().getTotalNoOfPages(),1);
        assertEquals(response.getData().getTotalNoOfElements(),3);
        assertEquals(response.getData().getContent().size(),3);
        assertNotNull(response);
    }

    @Test
    void ownTransactions() {
    }
    @Test
    void transactionByInterval(){
        Transactions firstTransaction = Transactions.builder().id(1).code("fakeCode").amount(10000.00)
                .customer_from(fromUser).customer_to(toUser).build();
        Transactions secondTransaction = Transactions.builder().id(1).code("fakeCode2").amount(20000.00)
                .customer_from(toUser).customer_to(fromUser).build();
        PageRequestObj pageRequestObj = PageRequestObj.builder().pageNumber(0).pageSize(3).build();
        Page<Transactions> transactions = new PageImpl<>(Arrays.asList(sampleTransaction,firstTransaction,secondTransaction));
        when(transactionRepo.findAllByTransactionTimeBetween(any(Date.class),any(Date.class),any(Pageable.class))).thenReturn(transactions);
        ApiResponse<PageableResponse> response = transactionServiceImpl.transactionByInterval("2022-10-12","2022-11-13",pageRequestObj);
        assertEquals(response.getData().getPageNumber(),0);
        assertEquals(response.getData().getPageSize(),3);
        assertEquals(response.getData().getTotalNoOfPages(),1);
        assertEquals(response.getData().getTotalNoOfElements(),3);
        assertEquals(response.getData().getContent().size(),3);
        assertNotNull(response);
    }
}