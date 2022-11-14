package com.securitytest.securitytest.resource;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String token;
    private String userName;
    private List<RoleDto> roles;
}
