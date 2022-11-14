package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.models.Privilege;
import com.securitytest.securitytest.repositories.PrivilegeRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.PrivilegeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PrivilegeServiceImplTest {
    @InjectMocks
    private PrivilegeServiceImpl privilegeServiceImpl;

    @Mock
    private PrivilegeRepo privilegeRepo;

    @Spy
    private ModelMapper modelMapper;
    private List<Privilege> privilegeList;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        privilegeList = Arrays.asList(new Privilege(1,"TEST_PRIVILEGE",null),
                new Privilege(2,"TEST_PRIVILEGE_2",null));
    }

    @Test
    void allPrivilege() {
        when(privilegeRepo.findAll()).thenReturn(privilegeList);
        ApiResponse<List<PrivilegeDto>> privileges = privilegeServiceImpl.allPrivilege();

        assertEquals(privileges.getData().size(),2);
        assertEquals(privileges.getData().get(0).getId(),1);
        assertEquals(privileges.getData().get(1).getName(),"TEST_PRIVILEGE_2");
    }

    @Test
    void privilegesByRole() {
    }
}