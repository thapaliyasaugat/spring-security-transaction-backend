package com.securitytest.securitytest.resource;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivilegeDto {
    private int id;
    private String name;
}
