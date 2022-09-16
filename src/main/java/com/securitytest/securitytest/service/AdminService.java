package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.UpdateRoleRequest;
import com.securitytest.securitytest.resource.UserDto;

public interface AdminService {
    UserDto updateRole(UpdateRoleRequest updateRoleRequest);

}
