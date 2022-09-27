package com.securitytest.securitytest.serviceImpl.transaction_chain_impl;

import com.securitytest.securitytest.resource.CashbackSchemeDto;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.CashbackService;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.TransactionChain;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class PerformCashback implements TransactionChain {
    private TransactionChain settlementChain;
    private RoleService roleService;
    private UserService userService;
    private CashbackService cashbackService;

    public PerformCashback() {
    }

    @Override
    public void setNextChain(TransactionChain settlementChain) {
        this.settlementChain = settlementChain;
    }

    @Override
    public void settlement(TransactionRequest request, UserDto fromUser, UserDto toUser) {
        List<RoleDto> userRoles = roleService.getUserRoles(fromUser.getEmail()).getData();
        log.info("checking available cashback schemes for {}",fromUser);
        List<CashbackSchemeDto> availableSchemes = checkAvailableCashback(userRoles);
        performCashback(availableSchemes,fromUser,request);
    }

    private List<CashbackSchemeDto> checkAvailableCashback(List<RoleDto> roles) {
        List<CashbackSchemeDto> availableSchemes = null;
        roles.stream().forEach(role ->{ roleService.getCashbackSchemeByRoleId(role.getId()).stream().forEach(r->availableSchemes.add(r));} );
        return availableSchemes;
    }
    private void performCashback(List<CashbackSchemeDto> cashbackSchemeDtos,UserDto fromUser,TransactionRequest request){
        log.info("Updating balance after determining cashback.");
        for (var scheme:cashbackSchemeDtos){
            log.info("updating balance for scheme {}",scheme);
            fromUser.setBalance(fromUser.getBalance()*(1+(scheme.getRewardRate()/100)));
            userService.updateUser(fromUser);
            //need to deduct from somewhere.
        }
    }

}
