package com.securitytest.securitytest.resource;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceLoadDetailDto {
    private int id;
    private String loadedBy;
    private String loadedFrom;
    private Double amount;
}
