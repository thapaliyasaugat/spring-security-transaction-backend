package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.ApiResponse;
import com.securitytest.securitytest.resource.RoleDto;
import com.securitytest.securitytest.resource.RoleRequest;
import com.securitytest.securitytest.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles(){
        ApiResponse<List<RoleDto>> roles = roleService.allRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> roleOfUser(@PathVariable int id){
        ApiResponse<List<RoleDto>> roles = roleService.roleOfUser(id);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ApiResponse<?> createNewRole(@Valid @RequestBody RoleRequest roleRequest){
        return roleService.createRole(roleRequest);
    }
    @PutMapping("/update/{id}")
    public ApiResponse<?> updateRole(@RequestBody RoleRequest roleRequest,@PathVariable int id){
        return null;
    }

    @GetMapping("/available-update/{id}")
    public ApiResponse<?> availableRoleToUpdate(@PathVariable int id){
        return roleService.userUpdateAvailableRoles(id);
    }
}
