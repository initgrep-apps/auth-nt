package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.LoginModel;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.providers.OptionalPasswordDaoAuthenticationProvider;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.service.AuthService;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AppUserDetailsManager userDetailsService;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private OptionalPasswordDaoAuthenticationProvider optionalPasswordDaoAuthenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthenticationProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    @Transactional
    @Override
    public TokenModel register(RegisterModel registerModel) {
        var userModel = UserMapper.toUserModel(registerModel);
        var encodedPassword = passwordEncoder.encode(registerModel.getPassword());
        userModel.setPassword(encodedPassword);
        userModel = userDetailsService.createUser(userModel);
        var authenticationToken = new UsernamePasswordAuthenticationToken(userModel, encodedPassword, userModel.getGrantedAuthorities());
        return tokenGenerator.createToken(authenticationToken);
    }

    @Override
    public TokenModel login(LoginModel loginModel) {
        String designatedUserName = !UtilMethods.isEmpty(loginModel.getPhoneNumber()) ? loginModel.getPhoneNumber() : loginModel.getEmail();
        UsernamePasswordAuthenticationToken unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(designatedUserName, loginModel.getPassword());
        UsernamePasswordAuthenticationToken authenticatedToken = (UsernamePasswordAuthenticationToken)optionalPasswordDaoAuthenticationProvider.authenticate(unauthenticatedToken);
        log.info("existing token details =- {}", authenticatedToken.getDetails());
        return tokenGenerator.createToken(authenticatedToken);
    }

    //token endpoint to get token after the login
    //before we issue a new token, we need to verify a refresh token
    // refresh token validity is 30days.
    // so if it has not expired or has more than week to expire, no point to issue a new one

    @Override
    public TokenModel getToken(TokenModel tokenModel) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenModel.getRefreshToken()));
        //additional checks to be done here
        // implement the functionality to enable revoking capabilities ( checks Max's previous video for ref.)

        Jwt jwt = (Jwt) authentication.getCredentials();
        //check if the jwt token present in DB and not revoked

        return tokenGenerator.createToken(authentication);
    }
}
