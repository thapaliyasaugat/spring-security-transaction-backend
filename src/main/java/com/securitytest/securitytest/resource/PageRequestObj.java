package com.securitytest.securitytest.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestObj {
    private int pageNumber;
    private int pageSize;
}
