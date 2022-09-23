package com.securitytest.securitytest.controller;

import com.securitytest.securitytest.resource.PageRequestObj;
import com.securitytest.securitytest.resource.UserDto;
import com.securitytest.securitytest.resource.UserPageableResponse;
import com.securitytest.securitytest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestBody PageRequestObj pageRequest){
        UserPageableResponse userList = userService.allUsers(pageRequest);
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
    @GetMapping("/me/detail")
    public UserDto ownDetail(){
        return userService.getMyDetail();
    }
}
