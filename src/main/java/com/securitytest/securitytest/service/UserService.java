package com.securitytest.securitytest.service;

import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;


public interface UserService {
    UserDto userById(int id);
    UserPageableResponse allUsers(PageRequest pageRequest);
    UserDto blockUser(int id);
    UserDto activateUser(int id);
    UserDto userByEmail(String email);
    UserDto save(User user);
    UserDto updateUser(UserDto userDto);
    void addUserRole(RoleDto roleDto,int id);
    UserDto getMyDetail();
}
