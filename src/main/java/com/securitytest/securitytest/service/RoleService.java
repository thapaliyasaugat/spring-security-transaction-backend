package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.RoleDto;

import java.util.List;

public interface RoleService {
    ApiResponse<List<RoleDto>> roleOfUser(int id);
    RoleDto findByName(String name);
    ApiResponse<List<RoleDto>> getUserRoles(String email);
}
