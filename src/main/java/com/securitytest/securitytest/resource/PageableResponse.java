package com.securitytest.securitytest.resource;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse implements Serializable {
    private List<TransactionDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalNoOfElements;
    private int totalNoOfPages;

}
