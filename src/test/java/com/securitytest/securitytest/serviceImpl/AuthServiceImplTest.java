package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.configuration.JwtConfiguration;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Spy
    private ModelMapper modelMapper;

    @Spy
    private PasswordEncoder passwordEncoder;
    @Spy
    private JwtConfiguration jwtConfiguration;
//    @Spy
//    private UserPrincipal userPrincipal;

    private User user;
    private Role role;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        role = new Role(2, "CUSTOMER",null,null,null);
        user = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
    }

    @Test
    void signInUser() {
        LoginRequest loginRequest= LoginRequest.builder().email("saugat@email.com").password("saugat22").build();
//        UserPrincipal userPrincipal = UserPrincipal.builder().id(1).userName("Saugat Thapaliya").email("saugat@email.com")
//                .password("fakefakefakefakefakefake").build();
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ApiResponse<JwtAuthResponse> response = authServiceImpl.signInUser(loginRequest);

        assertNotNull(response.getData());
        assertEquals(response.getData().getUserName(),"saugat@email.com");
    }

    @Test
    void signUpUser() {
        SignUpRequest signUpRequest = SignUpRequest.builder().userName("Saugat Thapaliya")
                .email("saugat@eamil.com").password("saugat22").build();
        RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        when(roleService.findByName(anyString())).thenReturn(roleDto);
        when(userService.save(any(User.class))).thenReturn(userDto);
        ApiResponse<?> response = authServiceImpl.signUpUser(signUpRequest);

        assertEquals(response.getStatus(),0);
        assertEquals(response.getMessage(),"User Created successfully.");
    }
}