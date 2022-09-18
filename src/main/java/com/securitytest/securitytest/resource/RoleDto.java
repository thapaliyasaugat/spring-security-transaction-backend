package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private int id;
    private RoleName name;
}
