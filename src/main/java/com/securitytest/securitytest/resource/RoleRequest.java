package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.Privilege;
import lombok.*;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoleRequest {
    private String name;
    private Set<Privilege> privileges;
}
