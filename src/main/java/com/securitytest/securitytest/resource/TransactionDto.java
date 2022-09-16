package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String code;
    private User customer_to;
    private Double amount;
    private User customer_from;
}
