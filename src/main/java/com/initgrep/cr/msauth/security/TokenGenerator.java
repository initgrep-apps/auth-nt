package com.initgrep.cr.msauth.security;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtAccessTokenConverter;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtRefreshTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TokenGenerator {

    @Autowired
    UserToJwtAccessTokenConverter userToJwtAccessTokenConverter;

    @Autowired
    UserToJwtRefreshTokenConverter userToJwtRefreshTokenConverter;

    @Value("${app.refresh-token.min-renewal-days}")
    private int refreshTokenMinRenewalDays;


    public InternalTokenModel createToken(Authentication authentication) {
        var accessToken = userToJwtAccessTokenConverter.convert(authentication);
        var refreshToken = resolveRefreshTokenAgainstExpiry(authentication);
        return new InternalTokenModel(accessToken, refreshToken);
    }

    private TokenModel resolveRefreshTokenAgainstExpiry(Authentication authentication) {
        if (shouldIssueFreshRefreshToken(authentication)) {
            return userToJwtRefreshTokenConverter.convert(authentication);
        }
        Jwt jwt = (Jwt) authentication.getCredentials();
        return new TokenModel(jwt.getId(), jwt.getTokenValue());

    }

    private boolean shouldIssueFreshRefreshToken(Authentication authentication) {
        if (!(authentication.getCredentials() instanceof Jwt)) {
            return true;
        }
        Jwt jwt = (Jwt) authentication.getCredentials();
        long days = Duration.between(Instant.now(), jwt.getExpiresAt()).toDays();
        return days < refreshTokenMinRenewalDays;
    }

}
