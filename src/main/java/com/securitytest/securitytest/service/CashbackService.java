package com.securitytest.securitytest.service;

import com.securitytest.securitytest.models.CashbackScheme;
import com.securitytest.securitytest.resource.*;

import java.util.List;

public interface CashbackService {
    ApiResponse<CashbackSchemeDto> createCashBackScheme(CashbackRequest cashbackRequest);
    ApiResponse<CashbackPageableResponse> allCashbackSchemes(PageRequestObj pageRequestObj);
    List<CashbackScheme> allCashbackSchemeList();
    ApiResponse<CashbackSchemeDto> cashbackSchemeById(int id);
    ApiResponse<CashbackSchemeDto> updateCashbackScheme(CashbackRequest cashbackRequest,int id);
    ApiResponse<CashbackSchemeDto> updateRolesOfCashbackScheme(CashbackRoleUpdateRequest roleUpdateRequest);
    ApiResponse<CashbackSchemeDto> deactivateCashbackScheme(int id);
    ApiResponse<CashbackSchemeDto> activateCashbackScheme(int id);
    ApiResponse<CashbackPageableResponse> schemesByInitiatedByEmail(PageRequestObj pageRequestObj,String email);
}
