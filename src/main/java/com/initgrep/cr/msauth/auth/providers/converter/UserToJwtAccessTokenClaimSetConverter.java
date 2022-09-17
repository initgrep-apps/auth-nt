package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.dto.SourceModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.RESOURCE_SERVER;
import static com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames.SCOPE;
import static java.util.Objects.isNull;

@Component
public class UserToJwtAccessTokenClaimSetConverter implements Converter<Authentication, JwtClaimsSet> {

    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.access-token.expiry-duration-min}")
    private int accessTokenExpiryMinutes;

    @Override
    public JwtClaimsSet convert(Authentication token) {
        UserModel user = (UserModel) token.getPrincipal();
        SourceModel source = (SourceModel) token.getDetails();
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .id(UtilMethods.guid())
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES))
                .subject(user.getIdentifier())
                .audience(List.of(RESOURCE_SERVER, source.getChannel()))
                .claim(SCOPE, getScopeFromAuthorities(user.getGrantedAuthorities()))
                .build();
    }

    public Set<String> getScopeFromAuthorities(Set<SimpleGrantedAuthority> authorities) {
        return
                isNull(authorities)
                        ? Collections.emptySet()
                        : authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
