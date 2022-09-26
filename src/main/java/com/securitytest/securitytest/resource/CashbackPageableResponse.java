package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashbackPageableResponse implements Serializable {
    private List<CashbackSchemeDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalNoOfElements;
    private int totalNoOfPages;
}
