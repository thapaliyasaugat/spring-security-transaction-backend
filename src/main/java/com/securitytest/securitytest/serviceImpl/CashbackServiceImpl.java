package com.securitytest.securitytest.serviceImpl;

import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.CashbackService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CashbackServiceImpl implements CashbackService {
    @Override
    public ApiResponse<CashbackSchemeDto> createCashBackScheme(CashbackRequest cashbackRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return null;
    }

    @Override
    public ApiResponse<CashbackPageableResponse> allCashbackSchemes() {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> cashbackSchemeById(int id) {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> updateCashbackScheme(CashbackRequest cashbackRequest, int id) {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> updateRolesOfCashbackScheme(CashbackRoleUpdateRequest roleUpdateRequest) {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> deactivateCashbackScheme(int id) {
        return null;
    }

    @Override
    public ApiResponse<CashbackSchemeDto> activateCashbackScheme(int id) {
        return null;
    }
}
