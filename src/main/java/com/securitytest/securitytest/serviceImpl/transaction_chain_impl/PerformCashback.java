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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PerformCashback implements TransactionChain {
    private TransactionChain settlementChain;
    private final RoleService roleService;
    private final UserService userService;
    private final CashbackService cashbackService;

    public PerformCashback(RoleService roleService, UserService userService, CashbackService cashbackService) {
        this.roleService = roleService;
        this.userService = userService;
        this.cashbackService = cashbackService;
    }

    @Override
    public void setNextChain(TransactionChain settlementChain) {
        this.settlementChain = settlementChain;
    }

    @Override
    public void settlement(TransactionRequest request, UserDto fromUser, UserDto toUser) {
        List<RoleDto> userRoles = roleService.getUserRoles(fromUser.getEmail()).getData();
        log.info("checking available cashback schemes for {}", fromUser);
        List<CashbackSchemeDto> availableSchemes = checkAvailableCashback(userRoles);
        performCashback(availableSchemes, fromUser, request);
    }

    private List<CashbackSchemeDto> checkAvailableCashback(List<RoleDto> roles) {
        log.info("Checking for available Cashback Schemes.");
        List<CashbackSchemeDto> availableSchemes = new ArrayList<>();
        roles.forEach(role -> {
            availableSchemes.addAll(roleService.getCashbackSchemeByRoleId(role.getId()));
        });
        return availableSchemes;
    }

    private void performCashback(List<CashbackSchemeDto> cashbackSchemeDtos, UserDto fromUser, TransactionRequest request) {
        log.info("Updating balance after determining cashback.");
        for (var scheme : cashbackSchemeDtos) {
            log.info("updating balance for scheme {}", scheme);
            fromUser.setBalance(fromUser.getBalance() * (1 + (scheme.getRewardRate() / 100)));
            userService.updateUser(fromUser);
            //save cashback
            //need to deduct from somewhere.
        }
    }

}
