package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.exceptions.ResourceNotFoundException;
import com.securitytest.securitytest.models.CashbackScheme;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.repositories.CashbackRepo;
import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.CashbackPageableResponse;
import com.securitytest.securitytest.resource.CashbackSchemeDto;
import com.securitytest.securitytest.resource.PageRequestObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class CashbackServiceImplTest {
    @Mock
    private CashbackRepo cashbackRepo;
    @InjectMocks
    private CashbackServiceImpl cashbackServiceImpl;
    @Spy
    private ModelMapper modelMapper;
    private Role role;
    private CashbackScheme cashbackScheme;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        role = new Role(2, RoleName.CUSTOMER, null);
        cashbackScheme = CashbackScheme.builder().id(1).subject("Dashain").description("2079")
                .active(true).initiatedBy("sauagt@email.com").rewardRate(2).eligibleRoles(new HashSet<>(Arrays.asList(role))).build();
    }

    @Test
    void createCashBackScheme() {

    }

    @Test
    void allCashbackSchemes() {
        CashbackScheme cashbackSchemeTihar = CashbackScheme.builder().id(2).subject("Tihar").description("2079")
                .active(true).initiatedBy("sauagt@email.com").rewardRate(2).eligibleRoles(new HashSet<>(Arrays.asList(role))).build();

        PageRequestObj pageRequestObj = PageRequestObj.builder().pageNumber(0).pageSize(2).build();
        Page<CashbackScheme> cashbackSchemes = new PageImpl<>(Arrays.asList(cashbackScheme, cashbackSchemeTihar));
        when(cashbackRepo.findAll(any(Pageable.class))).thenReturn(cashbackSchemes);
        ApiResponse<CashbackPageableResponse> response = cashbackServiceImpl.allCashbackSchemes(pageRequestObj);

        assertEquals(response.getStatus(), 0);
        assertEquals(response.getData().getTotalNoOfPages(),1);
        assertEquals(response.getData().getContent().size(),2);
        assertEquals(response.getData().getContent().get(1).getRewardRate(),2);
    }

    @Test
    void cashbackSchemeById() {
        when(cashbackRepo.findById(anyInt())).thenReturn(Optional.ofNullable(cashbackScheme));
        ApiResponse<CashbackSchemeDto> response = cashbackServiceImpl.cashbackSchemeById(1);
        assertEquals(response.getData().getId(),1);
        assertEquals(response.getData().getRewardRate(),2);
        assertNotNull(response.getData());
    }

    @Test
    void updateCashbackScheme() {
    }

    @Test
    void updateRolesOfCashbackScheme() {
    }

    @Test
    void deactivateCashbackScheme() {
    }

    @Test
    void activateCashbackScheme() {
    }
}