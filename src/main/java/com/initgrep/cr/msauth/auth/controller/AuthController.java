package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.auth.dto.LoginModel;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public TokenModel register(@RequestBody RegisterModel registerModel) {
        return  authService.register(registerModel);
    }

    @PostMapping("/login")
    public TokenModel login(@RequestBody LoginModel loginModel) {
        return authService.login(loginModel);
    }
    @PostMapping("/token")
    public TokenModel token(@RequestBody TokenModel tokenModel) {
        return authService.getToken(tokenModel);
    }


}
