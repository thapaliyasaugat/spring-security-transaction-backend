package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepo roleRepo, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDto> roleOfUser(int id) {
        List<Role> roles = roleRepo.roleOfUser(id);
        return roles.stream().map(role ->modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }
}
