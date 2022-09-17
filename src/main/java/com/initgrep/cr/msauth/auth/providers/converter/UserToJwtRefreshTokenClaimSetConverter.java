package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.AUTHORIZATION_SERVER;

@Component
public class UserToJwtRefreshTokenClaimSetConverter implements Converter<Authentication, JwtClaimsSet> {

    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.refresh-token.expiry-duration-days}")
    private int refreshTokenExpiryDays;

    @Override
    public JwtClaimsSet convert(Authentication token) {
        UserModel user = (UserModel) token.getPrincipal();
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .id(UtilMethods.guid())
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS))
                .subject(user.getIdentifier())
                .audience(Collections.singletonList(AUTHORIZATION_SERVER))
                .build();


    }
}
