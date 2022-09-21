package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public List<RoleDto> roleOfUser(int id) {
        List<Role> roles = roleRepo.roleOfUser(id);
        return roles.stream().map(role ->modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoleDto findByName(String name) {
        Role  role= roleRepo.findByName(RoleName.valueOf(name));
        return modelMapper.map(role,RoleDto.class);
    }
    @Override
    public List<RoleDto> getUserRoles(String email) {
        UserDto user = userService.userByEmail(email);
        List<Role> roles = roleRepo.roleOfUser(user.getId());
        return roles.stream().map(role->modelMapper.map(role,RoleDto.class)).collect(Collectors.toList());
    }
}
