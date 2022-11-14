package com.securitytest.securitytest.resource;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageableResponse {
    private List<UserDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalNoOfElements;
    private int totalNoOfPages;
}
