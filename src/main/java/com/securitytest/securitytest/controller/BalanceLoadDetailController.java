package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.PageRequestObj;
import com.securitytest.securitytest.service.BalanceLoadDetailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loadedbalancedetail")
public class BalanceLoadDetailController {
    private final BalanceLoadDetailService balanceLoadDetailService;

    public BalanceLoadDetailController(BalanceLoadDetailService balanceLoadDetailService) {
        this.balanceLoadDetailService = balanceLoadDetailService;
    }
    @PostMapping("/my")
    public ApiResponse<?> myBalanceLoadedHistory(@RequestBody PageRequestObj pageRequestObj){
        return balanceLoadDetailService.myBalanceLoadedDetail(pageRequestObj);
    }
    @PostMapping("/all")
    public ApiResponse<?> allBalanceLoadedHistory(@RequestBody PageRequestObj pageRequestObj){
        return balanceLoadDetailService.AllBalanceLoadedDetail(pageRequestObj);
    }
    @PostMapping("/email/{email}")
    public ApiResponse<?> balanceLoadedHistoryByEmail(@RequestBody PageRequestObj pageRequestObj,
                                                      @PathVariable String email){
        return balanceLoadDetailService.AllBalanceLoadedDetailByEmail(email,pageRequestObj);
    }
}
