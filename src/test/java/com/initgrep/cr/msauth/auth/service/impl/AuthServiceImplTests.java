package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.*;
import com.initgrep.cr.msauth.auth.providers.OptionalPasswordDaoAuthenticationProvider;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private OptionalPasswordDaoAuthenticationProvider optionalPasswordDaoAuthenticationProvider;

    @Mock
    private PasswordEncoder passwordEncoder;




    @Test
    void testRegister(){
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("test@emails.com");
        registerModel.setPassword("somePassword");
        registerModel.setFullName("some fullname");
        registerModel.setOtpInfo(new OtpInfoModel());

        UserModel userModel = UserModel.builder()
                .email(registerModel.getEmail())
                .fullName(registerModel.getFullName())
                .phoneNumber(registerModel.getPhoneNumber())
                .identifier(UUID.randomUUID().toString())
                .build();

        InternalTokenModel internalTokenModel = new InternalTokenModel();
        internalTokenModel.setAccessToken(new TokenModel("jit1","accessToken"));
        internalTokenModel.setRefreshToken(new TokenModel("jit2","refreshToken"));

        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userDetailsService.createUser(any(UserModel.class))).thenReturn(userModel);
        when(tokenService.provideToken(any())).thenReturn(internalTokenModel);

        TokenResponse tokenResponse = authService.register(registerModel);
        assertNotNull(tokenResponse);
    }
}
