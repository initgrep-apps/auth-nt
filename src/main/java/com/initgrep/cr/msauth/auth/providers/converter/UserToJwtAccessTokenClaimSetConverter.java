package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.RESOURCE_SERVER;
import static com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames.SCOPE;

@Component
public class UserToJwtAccessTokenClaimSetConverter implements Converter<Authentication, JwtClaimsSet> {

    @Autowired
    private AuthorityToScopeConverter authorityToScopeConverter;
    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.access-token.expiry-duration-min}")
    private int accessTokenExpiryMinutes;

    @Override
    public JwtClaimsSet convert(Authentication token) {
        UserModel user = (UserModel) token.getPrincipal();
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .id(UtilMethods.guid())
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES))
                .subject(user.getIdentifier())
                .audience(List.of(RESOURCE_SERVER))
                .claim(SCOPE, authorityToScopeConverter.convert(user.getGrantedAuthorities()))
                .build();
    }


}
