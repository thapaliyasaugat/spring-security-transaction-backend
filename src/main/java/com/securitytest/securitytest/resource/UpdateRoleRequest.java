package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {
    @NotEmpty(message = "specify whose role you want to update")
    private int updateRoleOfId;
    @NotEmpty(message = "specify role.")
    private String roleName;
}
