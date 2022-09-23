package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.configuration.UserPrincipal;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    UserRepo userRepo;
    @InjectMocks
    UserServiceImpl userServiceImpl;


    @Spy
    ModelMapper modelMapper;
    private Role role;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        role = new Role(2, RoleName.CUSTOMER);
        user = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
    }

    @Test
    void userById() {
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        UserDto saved = userServiceImpl.userById(1);
        assertEquals(saved.getBalance(), user.getBalance());
        assertNotEquals(saved.getUserName(), "saugat");
        assertNotNull(saved);
    }

    @Test
    void allUsers() {
        PageRequestObj pageRequest = PageRequestObj.builder().pageNumber(0).pageSize(3).build();
        User user1 = User.builder().id(1).userName("tik").email("tik@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        User user2 = User.builder().id(1).userName("John").email("john@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);
        userList.add(user2);
        Page<User> userPage = new PageImpl<>(userList);

        when(userRepo.findAll(any(Pageable.class))).thenReturn(userPage);
        UserPageableResponse response = userServiceImpl.allUsers(pageRequest);

        assertEquals(response.getPageNumber(), 0);
        assertEquals(response.getPageSize(), 3);
        assertEquals(response.getTotalNoOfElements(), 3);
        assertEquals(response.getContent().get(0).getUserName(), "Saugat");
        assertEquals(response.getTotalNoOfPages(), 1);
    }

    @Test
    void blockUser() {
        User updatedUser = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.BLOCKED)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
        UserDto blockedUser = userServiceImpl.blockUser(1);
//throw run time exception wwhrn already Blocked
//        assertThrows(RuntimeException.class,()->());
        assertNotNull(blockedUser);
        assertEquals(blockedUser.getStatus(),UserStatus.BLOCKED);
    }

    @Test
    void activateUser() {
        User testUser = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.BLOCKED)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(testUser));
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserDto activatedUser = userServiceImpl.activateUser(1);

        assertNotNull(activatedUser);
        assertEquals(activatedUser.getStatus(),UserStatus.ACTIVE);
    }

    @Test
    void userByEmail() {
        when(userRepo.findByEmail(anyString())).thenReturn(user);
        UserDto userDto = userServiceImpl.userByEmail("saugat@email.com");

        assertEquals(userDto.getUserName(), "Saugat");
        assertNotNull(userDto);

    }

    @Test
    void save() {
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserDto savedUser = userServiceImpl.save(user);

        assertEquals(savedUser.getUserName(), "Saugat");
        assertNotEquals(savedUser.getStatus(), UserStatus.BLOCKED);
    }

    @Test
    void updateUser() {
        when(userRepo.findByEmail(anyString())).thenReturn(user);
        UserDto userDto1 = UserDto.builder().id(1).userName("Changed").email("changed@email.com")
                .balance(30000.00).status(UserStatus.BLOCKED).build();
        User updatedUser = User.builder().id(1).userName("Changed").email("changed@email.com")
                .balance(30000.00).status(UserStatus.BLOCKED)
                .roles(new HashSet<>() {{
                    add(role);
                }}).build();
        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
        UserDto updatedUserDTO = userServiceImpl.updateUser(userDto1);

        assertEquals(updatedUserDTO.getUserName(), "Changed");
        assertEquals(updatedUserDTO.getBalance(), 30000.00);
        assertNotEquals(updatedUserDTO.getEmail(), user.getEmail());
        assertEquals(updatedUserDTO.getStatus(), updatedUser.getStatus());
        assertNotNull(updatedUserDTO);

    }

    @Test
    void addUserRole() {
        Role adminRole = new Role(1, RoleName.ADMIN);
        User roleUpdatedUser = User.builder().id(1).userName("Saugat").email("saugat@email.com")
                .password("passwordEncoder.encode(signUpRequest.getPassword())")
                .balance(50000.00).status(UserStatus.ACTIVE)
                .roles(new HashSet<>() {{
                    add(role);
                    add(adminRole);
                }}).build();

        RoleDto roleDto=RoleDto.builder().id(1).name(RoleName.ADMIN).build();
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepo.save(any(User.class))).thenReturn(roleUpdatedUser);

        userServiceImpl.addUserRole(roleDto,1); //returns nothing

    }

    @Test
    void getMyDetail() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("saugat@email.com");
//        UserPrincipal fakeUser = UserPrincipal.builder().id(1).userName("saugat").email("saugat@email.com")
//                .password("fdfdfdfd").build();
//      when(authentication.getPrincipal()).thenReturn(fakeUser);
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(("CUSTOMER").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//        System.out.println(authorities);

        UserDto userDto = userServiceImpl.getMyDetail();
        System.out.println(userDto);
    }
}