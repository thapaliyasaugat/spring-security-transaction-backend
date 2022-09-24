package com.securitytest.securitytest.resource;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestObj implements Serializable {
    private int pageNumber;
    private int pageSize;

    @Override
    public String toString() {
        return "PageRequestObj{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
