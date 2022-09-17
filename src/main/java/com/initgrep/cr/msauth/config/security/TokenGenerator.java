package com.initgrep.cr.msauth.config.security;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtAccessTokenClaimSetConverter;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtRefreshTokenClaimSetConverter;
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

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.INVALID_APP_TOKEN;

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    @Autowired
    UserToJwtAccessTokenClaimSetConverter userToJwtAccessTokenClaimSetConverter;

    @Autowired
    UserToJwtRefreshTokenClaimSetConverter userToJwtRefreshTokenClaimSetConverter;
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
        JwtClaimsSet claimSet = userToJwtAccessTokenClaimSetConverter.convert(authentication);
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        JwtClaimsSet claimSet = userToJwtRefreshTokenClaimSetConverter.convert(authentication);
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }
}
