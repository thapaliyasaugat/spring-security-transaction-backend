package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userList = userService.allUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id){
        return userService.userById(id);
    }
    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable String email){
        return userService.userByEmail(email);
    }
}
