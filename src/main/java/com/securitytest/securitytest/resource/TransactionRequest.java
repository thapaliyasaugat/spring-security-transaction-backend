package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotNull(message = "Do specify, who you want to send money")
    private int toUser;
    @NotNull(message = "Specify valid amount")
    private Double amount;
}
