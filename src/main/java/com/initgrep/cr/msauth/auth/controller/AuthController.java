package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.auth.dto.LoginModel;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.providers.OptionalPasswordDaoAuthenticationProvider;
import com.initgrep.cr.msauth.auth.util.UserMapper;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import com.initgrep.cr.msauth.config.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

//    @Autowired
//    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private OptionalPasswordDaoAuthenticationProvider optionalPasswordDaoAuthenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthenticationProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    //to register first time
    @PostMapping("/register")
    public TokenModel register(@RequestBody RegisterModel registerModel) {
        //verify if Otp is valid
        AppUser user = UserMapper.toEntityFromRegisterModel(registerModel);
        String encodedPassword = passwordEncoder.encode(registerModel.getPassword());
        user.setPassword(encodedPassword);
        userDetailsManager.createUser(user);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user, encodedPassword, Collections.emptyList());
        return tokenGenerator.createToken(authenticationToken);
    }

    // to login
    @PostMapping("/login")
    public TokenModel login(@RequestBody LoginModel loginModel) {
        //authenticate user via an authentication provider
        log.info("loginModel = {}", loginModel);
        //verify Otp
        String designatedUserName = !UtilMethods.isEmpty(loginModel.getPhoneNumber()) ? loginModel.getPhoneNumber() : loginModel.getEmail();

        //check if password is provided
        // if so - create a username and password authentication token
        // authenticate it via DaoAuthenticationProvider
        //else
        // load the user via designednated username
        // and authenticate mannually.

        Authentication authenticate = optionalPasswordDaoAuthenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(designatedUserName, loginModel.getPassword()));
        log.info("authenticated - {} , {} , {}", authenticate.isAuthenticated(), authenticate.getPrincipal(), authenticate.getCredentials());
        return tokenGenerator.createToken(authenticate);

    }

    //token endpoint to get token after the login
    //before we issue a new token, we need to verify a refresh token
    // refresh token validity is 30days. so if it has not expired or has more than week to expire, no point to issue a new one

    @PostMapping("/token")
    public TokenModel token(@RequestBody TokenModel tokenModel) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenModel.getRefreshToken()));
        //additional checks to be done here
        // implement the functionality to enable revoking capabilities ( checks Max's previous video for ref.)

        Jwt jwt = (Jwt) authentication.getCredentials();
        //check if the jwt token present in DB and not revoked

        return tokenGenerator.createToken(authentication);
    }


}
