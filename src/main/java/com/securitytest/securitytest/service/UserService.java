package com.securitytest.securitytest.service;

import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.*;

public interface UserService {
    ApiResponse<UserDto> userById(int id);
    ApiResponse<UserPageableResponse> allUsers(PageRequestObj pageRequest);
    ApiResponse<UserDto> blockUser(int id);
    ApiResponse<UserDto> activateUser(int id);
    UserDto userByEmail(String email);
    UserDto save(User user);
    UserDto updateUser(UserDto userDto);
    void addUserRole(RoleDto roleDto,int id);
    ApiResponse<UserDto> getMyDetail();
    ApiResponse<UserDto> loadBalance(LoadBalanceRequest loadBalanceRequest);
}
