package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    @NotEmpty(message = "UserName can't be empty")
    private String userName;

    @Email(message = "email is not valid.")
    @NotEmpty(message = "email can't be empty.")
    private String email;

    @NotEmpty(message = "password can't be empty")
    @Size(min = 6,message = "password must be greater than 6 character.")
    private String password;
}
