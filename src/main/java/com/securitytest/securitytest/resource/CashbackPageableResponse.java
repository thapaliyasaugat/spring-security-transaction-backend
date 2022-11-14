package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.CashbackScheme;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashbackPageableResponse {
    private List<CashbackScheme> content;
    private int pageNumber;
    private int pageSize;
    private long totalNoOfElements;
    private int totalNoOfPages;
}
