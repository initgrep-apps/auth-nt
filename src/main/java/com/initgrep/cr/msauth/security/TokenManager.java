package com.initgrep.cr.msauth.security;

import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtAccessTokenConverter;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtRefreshTokenConverter;
import com.initgrep.cr.msauth.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Setter
@Component
public class TokenManager {

    private final UserToJwtAccessTokenConverter userToJwtAccessTokenConverter;

    private final UserToJwtRefreshTokenConverter userToJwtRefreshTokenConverter;

    private final AppConfig appConfig;


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
        if ((authentication.getCredentials() instanceof Jwt)) {
            Jwt jwt = (Jwt) authentication.getCredentials();
            long days = Duration.between(Instant.now(), jwt.getExpiresAt()).toDays();
            return days < appConfig.getRefreshToken().getMinRenewalDays();
        }
        return true;
    }

}
