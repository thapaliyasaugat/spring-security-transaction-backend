package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.service.PrivilegeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/privilege")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> fetchAllPrivileges() {
        ApiResponse<?> privileges = privilegeService.allPrivilege();
        return new ResponseEntity<>(privileges, HttpStatus.OK);
    }
}
