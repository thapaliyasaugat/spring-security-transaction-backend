package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.RoleRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepo roleRepo, UserService userService, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<List<RoleDto>> roleOfUser(int id) {
        List<Role> roles = roleRepo.roleOfUser(id);
        return new ApiResponse<>(
                roles.stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList()),
                "roles of user ".concat(String.valueOf(id)),
                0
        );
    }

    @Override
    public ApiResponse<List<RoleDto>> allRoles() {
        List<Role> roles = roleRepo.findAll();
        List<RoleDto> roleDtoList = roles.stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
        return new ApiResponse<>(roleDtoList, "all Roles", 0);
    }

    @Override
    public RoleDto findByName(String name) {
        try {
            Role role = roleRepo.findByName(name);
            return modelMapper.map(role, RoleDto.class);
        } catch (Exception e) {
            log.error("error finding role by name {}", e.getMessage());
            throw new RuntimeException("No such Roles Defined.");
        }
    }

    @Override
    public ApiResponse<List<RoleDto>> getUserRoles(String email) {
        UserDto user = userService.userByEmail(email);
        List<Role> roles = roleRepo.roleOfUser(user.getId());
        return new ApiResponse<>(
                roles.stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList()),
                "roles of user ".concat(user.getUserName()),
                0
        );
    }

    @Override
    public ApiResponse<RoleDto> createRole(RoleRequest roleRequest) {
        try {
            log.info("Request to create role : {}", roleRequest);
            Role role = roleRepo.save(modelMapper.map(roleRequest, Role.class));
            return new ApiResponse<>(modelMapper.map(role, RoleDto.class), "Role Created Successfully.", 0);
        } catch (Exception e) {
            log.info("Exception on role creation : {}", e.getMessage());
            throw new RuntimeException("Error creating role. Check if roleName already exists.");
        }
    }

    @Override
    public ApiResponse<List<RoleDto>> userUpdateAvailableRoles(int id) {
        List<Role> rolesOfUser = roleRepo.roleOfUser(id);
        List<Role> allRoles = roleRepo.findAll();
        List<Role> availableRoles = allRoles.stream().filter(role -> !rolesOfUser.stream().anyMatch(r -> r.getName().equals(role.getName()))).collect(Collectors.toList());
        List<RoleDto> availableRolesDto = availableRoles.stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
        return new ApiResponse<>(availableRolesDto, "available role to update", 0);
    }
}
