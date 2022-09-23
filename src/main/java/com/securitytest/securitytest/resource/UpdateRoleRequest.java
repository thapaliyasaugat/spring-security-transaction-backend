package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {

    @NotEmpty(message = "specify whose role you want to update")
    private String updateRoleOfEmail;

    @NotEmpty(message = "specify role.")
    private String roleName;
}
