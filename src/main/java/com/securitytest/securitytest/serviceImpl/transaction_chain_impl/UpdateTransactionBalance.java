package com.securitytest.securitytest.serviceImpl.transaction_chain_impl;

import com.securitytest.securitytest.models.Transactions;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.TransactionRepo;
import com.securitytest.securitytest.resource.TransactionRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.TransactionChain;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateTransactionBalance implements TransactionChain {
    private TransactionChain settlementChain;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TransactionRepo transactionRepo;

    public UpdateTransactionBalance(UserService userService, ModelMapper modelMapper, TransactionRepo transactionRepo) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.transactionRepo = transactionRepo;
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
            saveTransaction(request,fromUser,toUser);
            this.settlementChain.settlement(request,fromUser,toUser);
        }catch (Exception e){
            log.error("Can't perform transaction. Error Updating balance for transaction {}",e.getMessage());
            throw new RuntimeException("Something goes wrong, Can't perform transaction.");
        }
    }
    private void saveTransaction(TransactionRequest request, UserDto fromUser, UserDto toUser){
        Transactions transactions = new Transactions();
        transactions.setAmount(request.getAmount());
        transactions.setCustomer_from(modelMapper.map(fromUser, User.class));
        transactions.setCustomer_to(modelMapper.map(toUser, User.class));
        transactionRepo.save(transactions);
    }
}
