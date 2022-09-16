package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.UserDto;

import java.util.List;


public interface UserService {
    UserDto userById(int id);
    List<UserDto> allUsers();
    UserDto blockUser(int id);
    UserDto activateUser(int id);
    UserDto userByEmail(String email);
}
