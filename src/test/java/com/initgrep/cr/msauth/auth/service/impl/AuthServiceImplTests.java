package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.*;
import com.initgrep.cr.msauth.auth.providers.OptionalPasswordDaoAuthenticationProvider;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.service.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTests {

    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    private AppUserDetailsManager userDetailsService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationProvider refreshTokenAuthProvider;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testRegister() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("test@email.com");
        registerModel.setPassword("somePassword");
        registerModel.setFullName("some fullname");
        registerModel.setOtpInfo(new OtpInfoModel());

        UserModel userModel = UserModel.builder()
                .email(registerModel.getEmail())
                .fullName(registerModel.getFullName())
                .phoneNumber(registerModel.getPhoneNumber())
                .identifier(UUID.randomUUID().toString())
                .build();


        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userDetailsService.createUser(any(UserModel.class))).thenReturn(userModel);
        when(tokenService.provideToken(any())).thenReturn(mockToken());

        TokenResponse tokenResponse = authService.register(registerModel);
        Assertions.assertThat(tokenResponse).isNotNull();
    }

    @Test
    void testLoginWithPhoneNumberAsUsername() {
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("test@email.com");
        loginModel.setPassword("password");
        loginModel.setOtpInfo(new OtpInfoModel());
        loginModel.setPhoneNumber("1234567890");

        when(tokenService.provideToken(any())).thenReturn(mockToken());
        TokenResponse tokenResponse = authService.login(loginModel);
        Assertions.assertThat(tokenResponse).isNotNull();
    }

    @Test
    void testLoginWithEmailAsUsername() {
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("test@email.com");
        loginModel.setPassword("password");
        loginModel.setOtpInfo(new OtpInfoModel());
        loginModel.setPhoneNumber("");

        when(tokenService.provideToken(any())).thenReturn(mockToken());
        TokenResponse tokenResponse = authService.login(loginModel);
        Assertions.assertThat(tokenResponse).isNotNull();
    }

    @Captor
    private ArgumentCaptor<BearerTokenAuthenticationToken> argumentCaptor;

    @Test
    void testGetNewToken() {

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("refreshtoken");

        Authentication authentication = new JwtAuthenticationToken(
                new Jwt("val", LocalDateTime.now().toInstant(ZoneOffset.UTC),
                        LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.UTC),
                        Map.of("header1", "something"), Map.of("scp", "something")));
        when(refreshTokenAuthProvider.authenticate(argumentCaptor.capture()))
                .thenReturn(authentication);
        when(tokenService.provideToken(any())).thenReturn(mockToken());
        TokenResponse tokenResponse = authService.getNewAccessToken(refreshTokenRequest);
        Assertions.assertThat(tokenResponse).isNotNull();
        System.out.println(argumentCaptor.getValue().getToken());
    }


    private InternalTokenModel mockToken() {
        InternalTokenModel internalTokenModel = new InternalTokenModel();
        internalTokenModel.setAccessToken(new TokenModel("jit1", "accessToken"));
        internalTokenModel.setRefreshToken(new TokenModel("jit2", "refreshToken"));
        return internalTokenModel;
    }
}
