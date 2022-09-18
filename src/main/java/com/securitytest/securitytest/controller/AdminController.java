package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.UpdateRoleRequest;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.AdminService;
import com.securitytest.securitytest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/test")
    public String adminTest(){
        return "ok";
    }

    @GetMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable int id){
        UserDto blockedUser = userService.blockUser(id);
        return new ResponseEntity<>(blockedUser, HttpStatus.OK);
    }
    @GetMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable int id){
        UserDto blockedUser = userService.activateUser(id);
        return new ResponseEntity<>(blockedUser, HttpStatus.OK);
    }
    @PutMapping("/update/role")
    public ResponseEntity<?>updateRoleOfUser(@Valid @RequestBody UpdateRoleRequest updateRoleRequest){
        UserDto user = adminService.updateRole(updateRoleRequest);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
