package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> roleOfUser(@PathVariable int id){
        List<RoleDto> roles = roleService.roleOfUser(id);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
