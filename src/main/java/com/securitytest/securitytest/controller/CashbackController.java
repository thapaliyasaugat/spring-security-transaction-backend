package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.CashbackService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cashback")
public class CashbackController {
    private final CashbackService cashbackService;

    public CashbackController(CashbackService cashbackService) {
        this.cashbackService = cashbackService;
    }

    @PostMapping("/create")
    public ApiResponse<CashbackSchemeDto> createCashbackScheme(@Valid @RequestBody CashbackRequest cashbackRequest) {
        return cashbackService.createCashBackScheme(cashbackRequest);
    }

    @PostMapping("/")
    public ApiResponse<CashbackPageableResponse> finAllCashbackScheme(@RequestBody PageRequestObj pageRequestObj) {
        return cashbackService.allCashbackSchemes(pageRequestObj);
    }
    @PostMapping("/created-by/email/{email}")
    public ApiResponse<CashbackPageableResponse> findCashbackSchemeByInitiatedByEmail(@RequestBody PageRequestObj pageRequestObj,@PathVariable String email){
        return cashbackService.schemesByInitiatedByEmail(pageRequestObj,email);
    }
    @GetMapping("/id/{id}")
    public ApiResponse<CashbackSchemeDto> cashbackSchemeById(@PathVariable int id){
        return cashbackService.cashbackSchemeById(id);
    }
    @PatchMapping("/deactivate/id/{id}")
    public ApiResponse<CashbackSchemeDto> deactivateCashbackScheme(@PathVariable int id){
        return cashbackService.deactivateCashbackScheme(id);
    }
    @PatchMapping("/activate/id/{id}")
    public ApiResponse<CashbackSchemeDto> activateCashbackScheme(@PathVariable int id){
        return cashbackService.activateCashbackScheme(id);
    }
}
