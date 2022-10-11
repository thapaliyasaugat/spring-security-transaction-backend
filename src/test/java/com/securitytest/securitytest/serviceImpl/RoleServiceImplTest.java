package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.resource.UserStatus;
import com.securitytest.securitytest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private UserService userService;

    @Spy
    private ModelMapper modelMapper;
    private User user;
    private Role role;

    @BeforeEach
    void setUp(){
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
    void roleOfUser() {
        Role adminRole =  new Role(1, "ADMIN",null,null,null);
        List<Role> roleList = Arrays.asList(adminRole,role);
        when(roleRepo.roleOfUser(anyInt())).thenReturn(roleList);
        ApiResponse<List<RoleDto>> returnedRoleOfUser = roleServiceImpl.roleOfUser(1);
        assertEquals(returnedRoleOfUser.getData().size(),2);
        assertEquals(returnedRoleOfUser.getData().get(0).getName(),"ADMIN");
        assertEquals(returnedRoleOfUser.getData().get(1).getName(),"CUSTOMER");
    }

    @Test
    void findByName() {
        when(roleRepo.findByName(anyString())).thenReturn(role);
        RoleDto roleDto = roleServiceImpl.findByName("CUSTOMER");
        assertEquals(roleDto.getName(),"CUSTOMER");
        assertNotNull(roleDto);
    }

    @Test
    void getUserRoles() {
        Role adminRole =  new Role(1, "ADMIN",null,null,null);
        List<Role> roleList = Arrays.asList(adminRole,role);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        when(userService.userByEmail(anyString())).thenReturn(userDto);
        when(roleRepo.roleOfUser(anyInt())).thenReturn(roleList);

        ApiResponse<List<RoleDto>> roleDtoList = roleServiceImpl.getUserRoles("saugat@email.com");
        assertEquals(roleDtoList.getData().size(),2);
        assertEquals(roleDtoList.getData().get(0).getName(),"ADMIN");
        assertNotNull(roleDtoList.getData());
    }
}