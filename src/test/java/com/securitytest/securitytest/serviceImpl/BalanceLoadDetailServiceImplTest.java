package com.securitytest.securitytest.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class BalanceLoadDetailServiceImplTest {
    @InjectMocks
    private BalanceLoadDetailServiceImpl balanceLoadDetailService;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void myBalanceLoadedDetail() {
    }

    @Test
    void allBalanceLoadedDetail() {
    }

    @Test
    void allBalanceLoadedDetailByEmail() {
    }
}