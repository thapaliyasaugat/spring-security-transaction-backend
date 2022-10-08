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
    private Date fromDate;
    private Date toDate;
    private Double fromAmount;
    private Double toAmount;

    @Override
    public String toString() {
        return "PageRequestObj{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
