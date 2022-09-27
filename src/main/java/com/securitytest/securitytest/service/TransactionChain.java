package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.resource.UserDto;

public interface TransactionChain {
    void setNextChain(TransactionChain settlementChain);
    void settlement(TransactionRequest request, UserDto fromUser, UserDto toUser);
}

