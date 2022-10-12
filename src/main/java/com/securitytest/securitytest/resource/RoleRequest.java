package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.Privilege;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoleRequest {
    @NotEmpty(message = "roleName can't be empty.")
    private String name;
    @NotNull(message = "Privileges can't be null.")
    private Set<Privilege> privileges;
}
