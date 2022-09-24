package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.JwtAuthResponse;
import com.securitytest.securitytest.resource.LoginRequest;
import com.securitytest.securitytest.resource.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse<JwtAuthResponse> signInUser(LoginRequest loginRequest);
    ApiResponse signUpUser(SignUpRequest signUpRequest);
}
