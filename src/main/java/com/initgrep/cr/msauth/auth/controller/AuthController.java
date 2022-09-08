package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.config.security.TokenGenerator;
import com.initgrep.cr.msauth.auth.dto.LoginDto;
import com.initgrep.cr.msauth.auth.dto.SingUpDto;
import com.initgrep.cr.msauth.auth.dto.TokenDto;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthenticationProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    //to register first time
    @PostMapping("/register")
    public TokenDto register(@RequestBody SingUpDto singupDTO){
        String encodedPassword  = passwordEncoder.encode(singupDTO.getPassword());
        AppUser user =  new AppUser(singupDTO.getUsername(),encodedPassword);
        userDetailsManager.createUser(user);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user,encodedPassword, Collections.emptyList());

        return tokenGenerator.createToken(authenticationToken);
    }
    // to login
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto){
        //authenticate user via an authentication provider
        log.info("loginDTO = {}", loginDto);
        Authentication authenticate = daoAuthenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword()));
        log.info("authenticated - {} , {} , {}", authenticate.isAuthenticated(), authenticate.getPrincipal(), authenticate.getCredentials());
        return tokenGenerator.createToken(authenticate);
    }

    //token endpoint to get token after the login
    //before we issue a new token, we need to verify a refresh token
    // refresh token validity is 30days. so if it has not expired or has more than week to expire, no point to issue a new one

    @PostMapping("/token")
    public TokenDto token(@RequestBody TokenDto tokenDto){
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDto.getRefreshToken()));
        //additional checks to be done here
        // implement the functionality to enable revoking capabilities ( checks Max's previous video for ref.)

        Jwt jwt = (Jwt) authentication.getCredentials();
        //check if the jwt token present in DB and not revoked

        return tokenGenerator.createToken(authentication);
    }


}
