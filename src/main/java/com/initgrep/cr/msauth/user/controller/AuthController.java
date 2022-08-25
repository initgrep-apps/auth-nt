package com.initgrep.cr.msauth.user.controller;

import com.initgrep.cr.msauth.config.security.TokenGenerator;
import com.initgrep.cr.msauth.user.dto.LoginDto;
import com.initgrep.cr.msauth.user.dto.SingUpDto;
import com.initgrep.cr.msauth.user.dto.TokenDto;
import com.initgrep.cr.msauth.user.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping("/register")
    public TokenDto register(@RequestBody SingUpDto singupDTO){
        AppUser user =  new AppUser(singupDTO.getUsername(), passwordEncoder.encode(singupDTO.getPassword()) );
        userDetailsManager.createUser(user);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user, singupDTO.getPassword(), Collections.emptyList());

        return tokenGenerator.createToken(authenticationToken);
    }
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto){
        //authenticate user via an authentication provider
        log.info("loginDTO = {}", loginDto);
        Authentication authenticate = daoAuthenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), passwordEncoder.encode(loginDto.getPassword())));
        log.info("authenticated - {} , {} , {}", authenticate.isAuthenticated(), authenticate.getPrincipal(), authenticate.getCredentials());
        return tokenGenerator.createToken(authenticate);
    }

}
