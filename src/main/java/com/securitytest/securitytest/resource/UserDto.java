package com.securitytest.securitytest.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private int id;
    private String userName;
    private String email;
    private UserStatus status;
    private Double balance;
}
