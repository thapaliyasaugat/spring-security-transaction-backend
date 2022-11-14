package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.UpdateRoleRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.AdminService;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final RoleService roleService;

    public AdminServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public ApiResponse<UserDto> updateRole(UpdateRoleRequest updateRoleRequest) {
        log.info("Request received to update :{} -> {}", updateRoleRequest.getUpdateRoleOfEmail(), updateRoleRequest.getRoleName());
        UserDto user = userService.userByEmail(updateRoleRequest.getUpdateRoleOfEmail());
        RoleDto role = roleService.findByName(updateRoleRequest.getRoleName());
        validate(updateRoleRequest, user, role);
        userService.addUserRole(role, user.getId());
        return new ApiResponse<>(user, "Role updated Successfully.", 0);
    }

    private void validate(UpdateRoleRequest updateRoleRequest, UserDto user, RoleDto role) {
        if (user == null)
            throw new ResourceNotFoundException("user", "email", updateRoleRequest.getUpdateRoleOfEmail());
        if (role == null) throw new ResourceNotFoundException("role", "name", updateRoleRequest.getRoleName());
        if (checkContainsRole(roleService.getUserRoles(user.getEmail()), role))
            throw new RuntimeException("User already have " + updateRoleRequest.getRoleName() + " role.");
    }

    private boolean checkContainsRole(ApiResponse<List<RoleDto>> roleList, RoleDto role) {
        return roleList.getData().stream().anyMatch(r -> r.getName().equals(role.getName()));
    }
}
