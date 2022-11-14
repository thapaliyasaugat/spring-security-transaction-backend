package com.securitytest.securitytest.resource;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private int id;
    private String userName;
    private String email;
    private UserStatus status;
    private Double balance;
}
