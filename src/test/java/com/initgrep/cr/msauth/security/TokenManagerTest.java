package com.initgrep.cr.msauth.security;

import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.providers.converter.AuthorityToScopeConverter;
import com.initgrep.cr.msauth.auth.providers.converter.ConverterTestUtil;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtAccessTokenConverter;
import com.initgrep.cr.msauth.auth.providers.converter.UserToJwtRefreshTokenConverter;
import com.initgrep.cr.msauth.config.AppConfig;
import liquibase.pro.packaged.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {
    private TokenManager tokenManager;
    private UserToJwtAccessTokenConverter userToJwtAccessTokenConverter;

    private UserToJwtRefreshTokenConverter userToJwtRefreshTokenConverter;

    private final AuthorityToScopeConverter authorityToScopeConverter = new AuthorityToScopeConverter();



    @Test
    void createToken() throws NoSuchAlgorithmException {
        new AuthorityToScopeConverter();
        userToJwtRefreshTokenConverter = new UserToJwtRefreshTokenConverter(
                authorityToScopeConverter, ConverterTestUtil.getJwtEncoder()
        );
        userToJwtRefreshTokenConverter.setRefreshTokenExpiryDays(30);
        userToJwtRefreshTokenConverter.setIssuerApp("test");

        userToJwtAccessTokenConverter = new UserToJwtAccessTokenConverter(
                authorityToScopeConverter, ConverterTestUtil.getJwtEncoder()
        );
        userToJwtAccessTokenConverter.setAccessTokenExpiryMinutes(5);
        userToJwtAccessTokenConverter.setIssuerApp("test");

        AppConfig appConfig = new AppConfig();
        appConfig.getRefreshToken().setMinRenewalDays(30);
        tokenManager = new TokenManager(userToJwtAccessTokenConverter, userToJwtRefreshTokenConverter,appConfig);

        UserModel user = UserModel.builder()
                .identifier(UUID.randomUUID().toString())
                .email("test@email.com")
                .phoneNumber("0000000000")
                .grantedAuthorities(Set.of(
                        new SimpleGrantedAuthority("X"),
                        new SimpleGrantedAuthority("Y")
                ))
                .fullName("Test Name")
                .build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = UsernamePasswordAuthenticationToken.unauthenticated(user, null);

        InternalTokenModel token = tokenManager.createToken(usernamePasswordAuthenticationToken);
        System.out.println(token.getAccessToken().getToken());
        Jwt jwt = ConverterTestUtil.getJwtDecoder().decode(token.getAccessToken().getToken());
        Object scp = jwt.getClaims().get("scope");
        System.out.println(scp);

    }
}