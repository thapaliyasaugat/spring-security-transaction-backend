package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
    @Email(message = "enter valid email.")
    @NotEmpty(message = "specify whose role you want to update")
    private String updateRoleOfEmail;

    @NotEmpty(message = "specify role.")
    private String roleName;
}
