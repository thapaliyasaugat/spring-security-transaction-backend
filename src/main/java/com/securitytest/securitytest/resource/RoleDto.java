package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.RoleName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private int id;
    private RoleName name;
}
