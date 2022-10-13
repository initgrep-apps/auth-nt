package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.*;
import com.initgrep.cr.msauth.auth.providers.OptionalPasswordDaoAuthenticationProvider;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.service.AuthService;
import com.initgrep.cr.msauth.auth.service.TokenService;
import com.initgrep.cr.msauth.auth.util.UserMapper;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private TokenService tokenService;

    @Autowired
    private OptionalPasswordDaoAuthenticationProvider optionalPasswordDaoAuthenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthenticationProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    @Transactional
    @Override
    public TokenResponse register(RegisterModel registerModel) {
        var userModel = UserMapper.toUserModel(registerModel);
        var encodedPassword = passwordEncoder.encode(registerModel.getPassword());
        userModel.setPassword(encodedPassword);
        userModel = userDetailsService.createUser(userModel);

         var authenticationToken
                 = UsernamePasswordAuthenticationToken.authenticated(userModel, encodedPassword, userModel.getGrantedAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return buildTokenResponse(tokenService.provideToken(authenticationToken));
    }

    @Override
    public TokenResponse login(LoginModel loginModel) {
        var designatedUserName = !UtilMethods.isEmpty(loginModel.getPhoneNumber()) ? loginModel.getPhoneNumber() : loginModel.getEmail();
        var unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(designatedUserName, loginModel.getPassword());
        var authenticatedToken = (UsernamePasswordAuthenticationToken) optionalPasswordDaoAuthenticationProvider.authenticate(unauthenticatedToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
        return buildTokenResponse(tokenService.provideToken(authenticatedToken));

    }

    @Override
    public TokenResponse getNewAccessToken(RefreshTokenRequest refreshTokenRequest) {
        var authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(refreshTokenRequest.getRefreshToken()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return buildTokenResponse(tokenService.provideToken(authentication));
    }

    private TokenResponse buildTokenResponse(InternalTokenModel internalTokenModel) {
        return TokenResponse.builder()
                .accessToken(internalTokenModel.getAccessToken().getToken())
                .refreshToken(internalTokenModel.getRefreshToken().getToken())
                .build();
    }

}
