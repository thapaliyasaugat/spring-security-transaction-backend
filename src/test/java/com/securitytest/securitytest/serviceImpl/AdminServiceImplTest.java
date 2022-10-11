package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {
    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Spy
    private ModelMapper modelMapper;
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
    void updateRole() {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        RoleDto roleDto = modelMapper.map(role,RoleDto.class);
        UpdateRoleRequest updateRoleRequest = UpdateRoleRequest.builder().updateRoleOfEmail("saugat@email.com")
                .roleName("TRANSACTIONS").build();
        Role adminRole =new Role(1, "ADMIN",null,null,null);
        when(userService.userByEmail(anyString())).thenReturn(userDto);
        RoleDto adminRoleDto = modelMapper.map(adminRole, RoleDto.class);
        when(roleService.findByName(anyString())).thenReturn(adminRoleDto);
        when(roleService.getUserRoles(anyString())).thenReturn(new ApiResponse<>(Collections.singletonList(roleDto),"",0));
        ApiResponse<UserDto> returnedUser = adminServiceImpl.updateRole(updateRoleRequest);

        assertNotNull(returnedUser.getData());
    }
    @Test
    void when_null_updateRoleRequest_throw_resourceException(){
        assertThrows(ResourceNotFoundException.class,()->{
            UpdateRoleRequest updateRoleRequest = UpdateRoleRequest.builder().updateRoleOfEmail("saugat@email.com")
                    .roleName("TRANSACTIONS").build();
            when(userService.userByEmail(anyString())).thenReturn(null);
            adminServiceImpl.updateRole(updateRoleRequest);
        });
    }
    @Test
    void when_invalid_updateRoleRequest_throw_runtimeException(){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        assertThrows(RuntimeException.class,()->{
            UpdateRoleRequest updateRoleRequest = UpdateRoleRequest.builder().updateRoleOfEmail("saugat@email.com")
                    .roleName("TRANSACT").build();
            when(userService.userByEmail(anyString())).thenReturn(userDto);
            adminServiceImpl.updateRole(updateRoleRequest);
        });
    }
}