package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.CashbackScheme;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.CashbackSchemeDto;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.RoleService;
import com.securitytest.securitytest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
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
                roles.stream().map(role ->modelMapper.map(role, RoleDto.class)).collect(Collectors.toList()),
                "roles of user ".concat(String.valueOf(id)),
                0
        );
    }

    @Override
    public RoleDto findByName(String name) {
        try {
            Role role = roleRepo.findByName(RoleName.valueOf(name));
            return modelMapper.map(role, RoleDto.class);
        }catch (Exception e){
            log.error("error finding role by name {}",e.getMessage());
            throw new RuntimeException("No such Roles Defined.");
        }
    }
    @Override
    public ApiResponse<List<RoleDto>> getUserRoles(String email) {
        UserDto user = userService.userByEmail(email);
        List<Role> roles = roleRepo.roleOfUser(user.getId());
        return new ApiResponse<>(
                roles.stream().map(role->modelMapper.map(role,RoleDto.class)).collect(Collectors.toList()),
                "roles of user ".concat(user.getUserName()),
                0
        );
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public List<CashbackSchemeDto> getCashbackSchemeByRoleId(int id) {
        Role role = roleRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("role","id",String.valueOf(id)));
        Set<CashbackScheme> schemes = role.getAttainableCashback();
        List<CashbackSchemeDto> schemeDtos = schemes.stream().map(scheme->modelMapper.map(scheme,CashbackSchemeDto.class)).collect(Collectors.toList());
        return schemeDtos;
    }
}
