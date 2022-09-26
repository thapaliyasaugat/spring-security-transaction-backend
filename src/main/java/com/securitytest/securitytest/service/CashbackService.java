package com.securitytest.securitytest.service;

import com.securitytest.securitytest.models.CashbackScheme;
import com.securitytest.securitytest.resource.*;

public interface CashbackService {
    ApiResponse<CashbackSchemeDto> createCashBackScheme(CashbackRequest cashbackRequest);
    ApiResponse<CashbackPageableResponse> allCashbackSchemes(PageRequestObj pageRequestObj);
    ApiResponse<CashbackSchemeDto> cashbackSchemeById(int id);
    ApiResponse<CashbackSchemeDto> updateCashbackScheme(CashbackRequest cashbackRequest,int id);
    ApiResponse<CashbackSchemeDto> updateRolesOfCashbackScheme(CashbackRoleUpdateRequest roleUpdateRequest);
    ApiResponse<CashbackSchemeDto> deactivateCashbackScheme(int id);
    ApiResponse<CashbackSchemeDto> activateCashbackScheme(int id);
    ApiResponse<CashbackPageableResponse> schemesByInitiatedByEmail(PageRequestObj pageRequestObj,String email);
}
