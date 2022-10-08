package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.*;
import com.securitytest.securitytest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestBody PageRequestObj pageRequest){
        ApiResponse<UserPageableResponse> userList = userService.allUsers(pageRequest);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUserById(@PathVariable int id){
        return userService.userById(id);
    }
    @GetMapping("/email/{email}")

    public UserDto getUserByEmail(@PathVariable String email){
        return userService.userByEmail(email);
    }
    @GetMapping("/me/detail")
    public ApiResponse<UserDto> ownDetail(){
        return userService.getMyDetail();
    }

    @PostMapping("/load/balance")
    public ApiResponse<?> loadBalance(@RequestBody LoadBalanceRequest loadBalanceRequest){
        return userService.loadBalance(loadBalanceRequest);
    }
}
