package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotEmpty(message = "UserName can't be empty")
    private String userName;
    @Email(message = "email is not valid.")
    @NotEmpty(message = "email can't be empty.")
    private String email;
    @Size(min = 6,message = "password must be greater than 6 character.")
    private String password;
}
