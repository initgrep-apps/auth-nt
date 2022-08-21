package com.initgrep.cr.msauth.user.controller;

import com.initgrep.cr.msauth.user.dto.UserModel;
import com.initgrep.cr.msauth.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserModel saveUser(@RequestBody UserModel userModel) {
        return userService.saveUser(userModel);
    }
	
}
