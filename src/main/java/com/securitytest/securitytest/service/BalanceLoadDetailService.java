package com.securitytest.securitytest.service;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.LoadBalancePageableResponse;
import com.securitytest.securitytest.resource.PageRequestObj;

public interface BalanceLoadDetailService {
    ApiResponse<LoadBalancePageableResponse> myBalanceLoadedDetail(PageRequestObj pageRequestObj);
    ApiResponse<LoadBalancePageableResponse> AllBalanceLoadedDetail(PageRequestObj pageRequestObj);
    ApiResponse<LoadBalancePageableResponse> AllBalanceLoadedDetailByEmail(String email,PageRequestObj pageRequestObj);
}
