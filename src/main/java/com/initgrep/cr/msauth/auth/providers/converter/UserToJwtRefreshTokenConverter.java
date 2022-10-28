package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.AUTHORIZATION_SERVER;
import static com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames.SCOPE;

@Setter
@RequiredArgsConstructor
@Component
public class UserToJwtRefreshTokenConverter implements Converter<Authentication, TokenModel> {

    private final AuthorityToScopeConverter authorityToScopeConverter;

    @Qualifier("jwtRefreshTokenEncoder")
    private final JwtEncoder refreshTokenEncoder;
    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.refresh-token.expiry-duration-days}")
    private int refreshTokenExpiryDays;

    @Override
    public TokenModel convert(Authentication authentication) {
        var user = (UserModel) authentication.getPrincipal();
        var now = Instant.now();
        var jit = UtilMethods.guid();
        var claimSet = JwtClaimsSet.builder()
                .id(jit)
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS))
                .subject(user.getIdentifier())
                .audience(Collections.singletonList(AUTHORIZATION_SERVER))
                .claim(SCOPE, authorityToScopeConverter.convert(user.getGrantedAuthorities()))
                .build();
        String token = refreshTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
        return new TokenModel(jit, token);
    }
}
