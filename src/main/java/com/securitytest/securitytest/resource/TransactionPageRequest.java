package com.securitytest.securitytest.resource;

import lombok.*;

import java.util.Date;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "PageRequestObj{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
