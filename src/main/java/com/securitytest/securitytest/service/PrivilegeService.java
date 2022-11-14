package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.PrivilegeDto;

import java.util.List;

public interface PrivilegeService {
    ApiResponse<List<PrivilegeDto>> allPrivilege();
    ApiResponse<List<PrivilegeDto>> privilegesByRole(String role);
}
