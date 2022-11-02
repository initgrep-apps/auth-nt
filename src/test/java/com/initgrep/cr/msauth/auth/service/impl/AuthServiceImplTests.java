package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.*;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.service.TokenService;
import com.initgrep.cr.msauth.config.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    AppConfig appConfig;

    @BeforeEach
    void setupAppConfig() {
        AppConfig.AccessTokenConfig accessTokenConfig = new AppConfig.AccessTokenConfig();
        accessTokenConfig.setExpiryDurationMin(5);
        when(appConfig.getAccessToken()).thenReturn(accessTokenConfig);
    }


    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("somePassword");
        registerRequest.setFullName("some full name");
        registerRequest.setOtpInfo(new OtpInfoModel());

        UserModel userModel = UserModel.builder()
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .identifier(UUID.randomUUID().toString())
                .build();


        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userDetailsService.createUser(any(UserModel.class))).thenReturn(userModel);
        when(tokenService.provideToken(any())).thenReturn(mockToken());

        TokenResponse tokenResponse = authService.register(registerRequest);
        Assertions.assertThat(tokenResponse).isNotNull();
    }

    @Test
    void testLoginWithPhoneNumberAsUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");
        loginRequest.setOtpInfo(new OtpInfoModel());
        loginRequest.setPhoneNumber("1234567890");

        when(tokenService.provideToken(any())).thenReturn(mockToken());
        TokenResponse tokenResponse = authService.login(loginRequest);
        Assertions.assertThat(tokenResponse).isNotNull();
    }

    @Test
    void testLoginWithEmailAsUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");
        loginRequest.setOtpInfo(new OtpInfoModel());
        loginRequest.setPhoneNumber("");

        when(tokenService.provideToken(any())).thenReturn(mockToken());
        TokenResponse tokenResponse = authService.login(loginRequest);
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
    }


    private InternalTokenModel mockToken() {
        InternalTokenModel internalTokenModel = new InternalTokenModel();
        internalTokenModel.setAccessToken(new TokenModel("jit1", "accessToken"));
        internalTokenModel.setRefreshToken(new TokenModel("jit2", "refreshToken"));
        return internalTokenModel;
    }
}
