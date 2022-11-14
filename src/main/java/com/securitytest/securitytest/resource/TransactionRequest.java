package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    @NotEmpty(message = "Enter valid email of user for transaction.")
    private String toUser;
    @NotNull(message = "Specify valid amount")
    private Double amount;
}
