package com.initgrep.cr.msauth.auth.provider;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionalPasswordDaoAuthenticationProviderTest {

    @InjectMocks
    OptionalPasswordDaoAuthenticationProvider authenticationProvider;

    @Spy
    PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

    @Mock
    AppUserDetailsManager userDetailsManager;

    @Spy
    List<String> spyList =  new ArrayList<>();

    @Test
    void additionalAuthenticationChecks_withEmptyPassword(){
        var userDetails = UserModel.builder()
                .password("password")
                .build();
        UsernamePasswordAuthenticationToken token
                =  UsernamePasswordAuthenticationToken.authenticated(userDetails,"", null);
        authenticationProvider.additionalAuthenticationChecks(userDetails, token);
        verify(passwordEncoder, never()).matches(any(CharSequence.class), anyString());
    }

    @Test
    void additionalAuthenticationChecks_withPasswordMatch(){
        var encodedPassword =  passwordEncoder.encode("password");
        var userDetails = UserModel.builder()
                .password(encodedPassword)
                .build();
        UsernamePasswordAuthenticationToken token
                =  UsernamePasswordAuthenticationToken.authenticated(userDetails,"password", null);
        authenticationProvider.additionalAuthenticationChecks(userDetails, token);
        verify(passwordEncoder, times(1)).matches(any(CharSequence.class), anyString());
    }

    @Test
    void additionalAuthenticationChecks_withPasswordNonMatch(){
        var encodedPassword =  passwordEncoder.encode("password");
        var userDetails = UserModel.builder()
                .password(encodedPassword)
                .build();
        UsernamePasswordAuthenticationToken token
                =  UsernamePasswordAuthenticationToken.authenticated(userDetails,"differentPassword", null);

        Assertions.assertThrows(BadCredentialsException.class,
                () -> authenticationProvider.additionalAuthenticationChecks(userDetails, token));
        verify(passwordEncoder, times(1)).matches(any(CharSequence.class), anyString());
    }



}