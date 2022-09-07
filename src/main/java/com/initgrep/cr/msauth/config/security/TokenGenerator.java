package com.initgrep.cr.msauth.config.security;

import com.initgrep.cr.msauth.auth.dto.TokenDto;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder  accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;


    private String createAccessToken(Authentication authentication){
        AppUser appUser = (AppUser) authentication.getPrincipal();
        Instant now =  Instant.now();

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .issuer("ms-auth")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(String.valueOf(appUser.getId()))
                .claim("username", appUser.getUsername())
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication){
        AppUser appUser = (AppUser) authentication.getPrincipal();
        Instant now =  Instant.now();

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .issuer("ms-auth")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(String.valueOf(appUser.getId()))
                .claim("username", appUser.getUsername())
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

    public TokenDto createToken(Authentication authentication){
        if(!(authentication.getPrincipal() instanceof  AppUser )){
            throw new BadCredentialsException("principal is not valid");
        }
        AppUser user =  (AppUser) authentication.getPrincipal();
        TokenDto tokenDto = new TokenDto();
        tokenDto.setUserId(String.valueOf(user.getId()));
        tokenDto.setAccessToken(createAccessToken(authentication));

        String refreshToken;

        if(authentication.getCredentials() instanceof Jwt){
            Jwt jwt =  (Jwt) authentication.getCredentials();

            Instant now  = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();

            long days = Duration.between(now, expiresAt).toDays();
            if(days < 7){
                refreshToken =  createRefreshToken(authentication);
            }else{
                refreshToken = jwt.getTokenValue();;
            }
        }else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDto.setRefreshToken(refreshToken);
        return tokenDto;
    }
}
