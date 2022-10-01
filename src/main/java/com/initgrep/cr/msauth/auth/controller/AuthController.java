package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.auth.dto.LoginModel;
import com.initgrep.cr.msauth.auth.dto.RefreshTokenRequest;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenResponse;
import com.initgrep.cr.msauth.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody @Valid RegisterModel registerModel) {
        return authService.register(registerModel);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginModel loginModel) {
        return authService.login(loginModel);
    }

    @PostMapping("/token")
    public TokenResponse token(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.getNewAccessToken(refreshTokenRequest);
    }


}
