package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.User;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {
    private int id;
    private String code;
    private UserDto customer_to;
    private Double amount;
    private UserDto customer_from;
}
