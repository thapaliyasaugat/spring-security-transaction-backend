package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {
    private int id;
    private String code;
    private UserDto customer_to;
    private Double amount;
    private UserDto customer_from;
}
