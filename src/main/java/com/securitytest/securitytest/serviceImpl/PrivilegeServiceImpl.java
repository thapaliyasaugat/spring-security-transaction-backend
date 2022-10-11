package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Privilege;
import com.securitytest.securitytest.repositories.PrivilegeRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.PrivilegeDto;
import com.securitytest.securitytest.service.PrivilegeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepo privilegeRepo;
    private final ModelMapper modelMapper;

    public PrivilegeServiceImpl(PrivilegeRepo privilegeRepo, ModelMapper modelMapper) {
        this.privilegeRepo = privilegeRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<List<PrivilegeDto>> allPrivilege() {
        List<Privilege> privileges = privilegeRepo.findAll();
        List<PrivilegeDto> privilegeDtoList = privileges.stream().map(privilege -> modelMapper.map(privilege, PrivilegeDto.class)).collect(Collectors.toList());
        return new ApiResponse<>(privilegeDtoList,"all privileges",0);
    }

    @Override
    public ApiResponse<List<PrivilegeDto>> privilegesById(String role) {
        return null;
    }
}
