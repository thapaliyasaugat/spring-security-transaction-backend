package com.securitytest.securitytest.serviceImpl.transaction_chain_impl;

import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.TransactionChain;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class UpdateTransactionBalance implements TransactionChain {
    private TransactionChain settlementChain;
    private UserService userService;

    public UpdateTransactionBalance() {
    }

    @Override
    public void setNextChain(TransactionChain settlementChain) {
        this.settlementChain=settlementChain;
    }

    @Override
    public void settlement(TransactionRequest request, UserDto fromUser, UserDto toUser) {
        try {
            log.info("updating balance for transaction {}",request);
            fromUser.setBalance(fromUser.getBalance() - request.getAmount());
            userService.updateUser(fromUser);
            toUser.setBalance(toUser.getBalance() + request.getAmount());
            userService.updateUser(toUser);
            this.settlementChain.settlement(request,fromUser,toUser);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong, Can't perform transaction.");
        }
    }
}
