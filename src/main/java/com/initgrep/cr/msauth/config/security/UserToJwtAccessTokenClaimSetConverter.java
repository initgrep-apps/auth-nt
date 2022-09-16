package com.initgrep.cr.msauth.config.security;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Component
public class UserToJwtAccessTokenClaimSetConverter implements Converter<UserModel, JwtClaimsSet> {

    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.access-token.expiry-duration-min}")
    private int accessTokenExpiryMinutes;

    @Override
    public JwtClaimsSet convert(UserModel user) {
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES))
                .subject(user.getIdentifier())
                .claim("scope", getScopeFromAuthorities(user.getGrantedAuthorities()))
                .build();


    }

    public String getScopeFromAuthorities(Set<SimpleGrantedAuthority> authorities) {
        return
                isNull(authorities)
                        ? ""
                        : authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }
}
