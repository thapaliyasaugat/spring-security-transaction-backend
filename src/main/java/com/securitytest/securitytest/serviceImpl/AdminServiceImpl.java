package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.resource.UpdateRoleRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public AdminServiceImpl(UserRepo userRepo, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto updateRole(UpdateRoleRequest updateRoleRequest) {
        User user = userRepo.findById(updateRoleRequest.getUpdateRoleOfId()).orElseThrow(()->new ResourceNotFoundException("user","id",String.valueOf(updateRoleRequest.getUpdateRoleOfId())));
        Role role = roleRepo.findByName(RoleName.valueOf(updateRoleRequest.getRoleName()));
        if(role == null) throw new ResourceNotFoundException("role","name",updateRoleRequest.getRoleName());
        if(user.getRoles().contains(role)) throw new RuntimeException("User already have "+ updateRoleRequest.getRoleName() +" role.");
        user.addRole(role);
        User updatedUser = userRepo.save(user);
        return modelMapper.map(updatedUser,UserDto.class);
    }

}
