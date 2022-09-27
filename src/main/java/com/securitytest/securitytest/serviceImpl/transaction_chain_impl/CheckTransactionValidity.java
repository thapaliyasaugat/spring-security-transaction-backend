package com.securitytest.securitytest.serviceImpl.transaction_chain_impl;

import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.resource.UserStatus;
import com.securitytest.securitytest.service.TransactionChain;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class CheckTransactionValidity implements TransactionChain {
    private TransactionChain settlementChain;

    @Override
    public void setNextChain(TransactionChain settlementChain) {
        this.settlementChain = settlementChain;
    }

    @Override
    public void settlement(TransactionRequest request,UserDto fromUser ,UserDto toUser) {
        log.info("Request to validate Transaction {}",request);
        boolean valid =  checkUserAndAmountValidity(request,fromUser,toUser);
        if(valid){
            this.settlementChain.settlement(request,fromUser,toUser);
        }
    }
    private boolean checkUserAndAmountValidity(TransactionRequest transactionRequest,UserDto fromUser,UserDto toUser){
        if(fromUser.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("Can't perform transaction. Your account is blocked.");
        if(toUser.getStatus().equals(UserStatus.BLOCKED)) throw new RuntimeException("Can't perform transaction. The account you are trying to send Money is currently blocked.");
        if(fromUser.getId() == toUser.getId()) throw new RuntimeException("You can't make transaction to yourself");
        if(transactionRequest.getAmount()>50000) throw new RuntimeException("Can't make transaction above 50,000.");
        if(transactionRequest.getAmount()<500) throw new RuntimeException("Can't make transaction below 500. ");
        if(fromUser.getBalance()<transactionRequest.getAmount()) throw new RuntimeException("You don't have enough balance.");
        return true;
    }
}
