package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.configuration.JwtConfiguration;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.AuthService;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userAuthService")
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.modelMapper = modelMapper;
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
    @Transactional
    public ApiResponse signUpUser(SignUpRequest signUpRequest) {
        RoleDto role = roleService.findByName(RoleName.CUSTOMER.toString());
        if(role==null) throw new RuntimeException("No Role Specified");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(modelMapper.map(role,Role.class));
        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(roleSet);
        user.setBalance(50000.00);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userService.save(user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(0);
        apiResponse.setMessage("User Created successfully.");
        return apiResponse;
    }

}
