package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> roleOfUser(int id);
    RoleDto findByName(String name);
    List<RoleDto> getUserRoles(String email);
}
