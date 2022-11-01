package com.initgrep.cr.msauth.security;

import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.providers.converter.*;
import com.initgrep.cr.msauth.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.AUTHORIZATION_SERVER;
import static com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames.SCOPE;
import static org.assertj.core.api.Assertions.assertThat;

class TokenManagerTest {
    private TokenManager tokenManager;

    private final AuthorityToScopeConverter authorityToScopeConverter = new AuthorityToScopeConverter();
    private final ScopeToAuthorityConverter scopeToAuthorityConverter = new ScopeToAuthorityConverter();


    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        UserToJwtRefreshTokenConverter userToJwtRefreshTokenConverter = new UserToJwtRefreshTokenConverter(
                authorityToScopeConverter, ConverterTestUtil.getJwtEncoder()
        );
        userToJwtRefreshTokenConverter.setRefreshTokenExpiryDays(30);
        userToJwtRefreshTokenConverter.setIssuerApp("test");

        var userToJwtAccessTokenConverter = new UserToJwtAccessTokenConverter(
                authorityToScopeConverter, ConverterTestUtil.getJwtEncoder()
        );
        userToJwtAccessTokenConverter.setAccessTokenExpiryMinutes(5);
        userToJwtAccessTokenConverter.setIssuerApp("test");

        AppConfig appConfig = new AppConfig();
        appConfig.getRefreshToken().setExpiryDurationDays(30);
        appConfig.getRefreshToken().setMinRenewalDays(5);
        tokenManager = new TokenManager(userToJwtAccessTokenConverter, userToJwtRefreshTokenConverter, appConfig);

    }


    @Test
    void createToken()  {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = UsernamePasswordAuthenticationToken.unauthenticated(getMockUser(), null);

        InternalTokenModel token = tokenManager.createToken(usernamePasswordAuthenticationToken);
        assertValidAccessToken(token);
        assertValidRefreshToken(token);
    }

    @Test
    void createTokenAndReuseExistingRefreshToken() throws NoSuchAlgorithmException {

        Instant now = Instant.now();
        // 1) create a token pair
        var claimSet = JwtClaimsSet.builder()
                .id("ID")
                .issuer("test")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject("USER_ID")
                .audience(Collections.singletonList(AUTHORIZATION_SERVER))
                .claim(SCOPE, authorityToScopeConverter.convert(Collections.emptySet()))
                .build();
        Jwt refreshTokenJwt = ConverterTestUtil.getJwtEncoder().encode(JwtEncoderParameters.from(claimSet));
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(getMockUser(), refreshTokenJwt, null);
        var token = tokenManager.createToken(authentication);
        assertValidAccessToken(token);
        assertValidRefreshToken(token);
    }

    @Test
    void createTokenAndNotReuseExistingRefreshToken() throws NoSuchAlgorithmException {

        Instant now = Instant.now();
        // 1) create a token pair
        var claimSet = JwtClaimsSet.builder()
                .id("ID")
                .issuer("test")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject("USER_ID")
                .audience(Collections.singletonList(AUTHORIZATION_SERVER))
                .claim(SCOPE, authorityToScopeConverter.convert(Collections.emptySet()))
                .build();
        Jwt refreshTokenJwt = ConverterTestUtil.getJwtEncoder().encode(JwtEncoderParameters.from(claimSet));
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(getMockUser(), refreshTokenJwt, null);
        var token = tokenManager.createToken(authentication);
        assertValidAccessToken(token);
        assertValidRefreshToken(token);
    }

    private void assertValidAccessToken(InternalTokenModel token) {
        Jwt jsonWebToken = ConverterTestUtil.getJwtDecoder().decode(token.getAccessToken().getToken());
        assertThat(jsonWebToken).satisfies(jwt -> {
            String scp = (String) jwt.getClaims().get("scope");
            assertThat(scp).contains("X");
            assertThat(scp).contains("Y");
            assertThat(jwt.getId()).isNotBlank();
            assertThat(jwt.getAudience().toString()).isNotBlank();
            assertThat(jwt.getSubject()).isNotBlank();
            Instant expiresAt = jwt.getExpiresAt();
            assertThat(expiresAt).isNotNull();
            LocalDateTime expiryDate = LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault()).withNano(0).withSecond(0);
            LocalDateTime now = LocalDateTime.now().withNano(0).withSecond(0);
            assertThat(expiryDate).isEqualTo(now.plusMinutes(5));
        });

    }


    private void assertValidRefreshToken(InternalTokenModel token) {
        Jwt jsonWebToken = ConverterTestUtil.getJwtDecoder().decode(token.getRefreshToken().getToken());
        assertThat(jsonWebToken).satisfies(jwt -> {
            String scp = (String) jwt.getClaims().get("scope");

            assertThat(jwt.getId()).isNotBlank();
            assertThat(jwt.getAudience().toString()).isNotBlank();
            assertThat(jwt.getSubject()).isNotBlank();
            Instant expiresAt = jwt.getExpiresAt();
            assertThat(expiresAt).isNotNull();
            LocalDate expiryDate = LocalDate.ofInstant(expiresAt, ZoneId.systemDefault());
            LocalDate now = LocalDate.now();
            assertThat(expiryDate).isEqualTo(now.plusDays(30));
        });
    }

    private UserModel getMockUser() {
        return UserModel.builder()
                .identifier(UUID.randomUUID().toString())
                .email("test@email.com")
                .phoneNumber("0000000000")
                .grantedAuthorities(Set.of(
                        new SimpleGrantedAuthority("X"),
                        new SimpleGrantedAuthority("Y")
                ))
                .fullName("Test Name")
                .build();
    }
}