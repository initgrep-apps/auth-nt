package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.auth.dto.LoginRequest;
import com.initgrep.cr.msauth.auth.dto.RefreshTokenRequest;
import com.initgrep.cr.msauth.auth.dto.RegisterRequest;
import com.initgrep.cr.msauth.auth.dto.TokenResponse;
import com.initgrep.cr.msauth.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/token")
    public TokenResponse token(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.getNewAccessToken(refreshTokenRequest);
    }


}
