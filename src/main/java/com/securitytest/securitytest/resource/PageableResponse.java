package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse {
    private List<TransactionDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalNoOfElements;
    private int totalNoOfPages;

}
