package com.securitytest.securitytest.resource;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionPageRequest {
    private int pageNumber;
    private int pageSize;
    private String filter;
    private String fromDate;
    private String toDate;
    private Double fromAmount;
    private Double toAmount;
    private String code;
}
