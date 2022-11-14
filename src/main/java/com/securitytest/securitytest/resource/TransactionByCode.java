package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionByCode {
    @NotEmpty(message = "code can't be empty.")
    private String code;
}
