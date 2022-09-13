package com.initgrep.cr.msauth.config.security;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.INVALID_APP_TOKEN;

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    @Value("${spring.application.name}")
    private String issuerApp;

    @Value("${app.access-token.expiry-duration-min}")
    private int accessTokenExpiryMinutes;

    @Value("${app.refresh-token.expiry-duration-days}")
    private int refreshTokenExpiryDays;

    @Value("${app.refresh-token.min-renewal-days}")
    private int refreshTokenMinRenewalDays;

    public TokenModel createToken(Authentication authentication) {
        throwIfNotAppUser(authentication);
        TokenModel tokenModel = new TokenModel();
        tokenModel.setAccessToken(createAccessToken(authentication));
        tokenModel.setRefreshToken(resolveRefreshTokenAgainstExpiry(authentication));
        return tokenModel;
    }

    private String resolveRefreshTokenAgainstExpiry(Authentication authentication) {
        if (shouldIssueFreshRefreshToken(authentication)) {
            return createRefreshToken(authentication);
        } else {
            Jwt jwt = (Jwt) authentication.getCredentials();
            return jwt.getTokenValue();
        }
    }

    private boolean shouldIssueFreshRefreshToken(Authentication authentication) {
        if (!(authentication.getCredentials() instanceof Jwt)) {
            return true;
        }
        Jwt jwt = (Jwt) authentication.getCredentials();
        long days = Duration.between(Instant.now(), jwt.getExpiresAt()).toDays();
        return days < refreshTokenMinRenewalDays;
    }

    private void throwIfNotAppUser(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserModel)) {
            throw new BadCredentialsException(INVALID_APP_TOKEN);
        }
    }

    private String createAccessToken(Authentication authentication) {
        UserModel userModel = (UserModel) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .issuer(issuerApp)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES))
                .subject(userModel.getEmail())
                //extra claims to be added here.
                //make sure to get all claims from the token in tokenToUserConverter or
                // let default jwtConverter do the work BUT i have not tested it yet
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        UserModel userModel = (UserModel) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .issuer("ms-auth")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS))
                .subject(userModel.getEmail())
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }
}
