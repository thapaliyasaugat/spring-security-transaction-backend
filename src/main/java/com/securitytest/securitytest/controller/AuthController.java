package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.JwtAuthResponse;
import com.securitytest.securitytest.resource.LoginRequest;
import com.securitytest.securitytest.resource.SignUpRequest;
import com.securitytest.securitytest.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/signup")
    public ApiResponse signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){
        return authService.signUpUser(signUpRequest);
    }

    @PostMapping("/signin")
    public JwtAuthResponse signInUser(@Valid @RequestBody LoginRequest loginRequest){
        return authService.signInUser(loginRequest);
    }
}
