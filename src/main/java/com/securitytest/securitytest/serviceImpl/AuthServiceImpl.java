package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.configuration.JwtConfiguration;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userAuthService")
public class AuthServiceImpl implements AuthService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;

    public AuthServiceImpl(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public JwtAuthResponse signInUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtConfiguration.generateToken(authentication);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(jwt);
        jwtAuthResponse.setUserName(authentication.getName());
        return jwtAuthResponse;
    }

    @Override
    public ApiResponse signUpUser(SignUpRequest signUpRequest) {
        Role role = roleRepo.findByName(RoleName.CUSTOMER);
        if(role==null) throw new RuntimeException("No Role Specified");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setStatus(UserStatus.ACTIVE.status);
        user.setRoles(roleSet);
        user.setBalance(50000.00);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepo.save(user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(0);
        apiResponse.setMessage("User Created successfully.");
        return apiResponse;
    }

}
