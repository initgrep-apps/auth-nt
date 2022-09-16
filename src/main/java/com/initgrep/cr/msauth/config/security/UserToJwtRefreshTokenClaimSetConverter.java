package com.initgrep.cr.msauth.config.security;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class UserToJwtRefreshTokenClaimSetConverter implements Converter<UserModel, JwtClaimsSet> {

    @Value("${app.refresh-token.expiry-duration-days}")
    private int refreshTokenExpiryDays;

    @Override
    public JwtClaimsSet convert(UserModel user) {
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("ms-auth")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS))
                .subject(user.getIdentifier())
                .build();


    }
}
