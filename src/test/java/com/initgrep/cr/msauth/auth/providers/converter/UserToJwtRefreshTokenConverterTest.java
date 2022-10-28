package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserToJwtRefreshTokenConverterTest {

    @Test
    void convert() throws NoSuchAlgorithmException {
        UserToJwtRefreshTokenConverter converter = new UserToJwtRefreshTokenConverter(
                new AuthorityToScopeConverter(),
                ConverterTestUtil.getJwtEncoder()
        );
        converter.setRefreshTokenExpiryDays(30);
        converter.setIssuerApp("test");
        
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
        TokenModel tokenModel = converter.convert(usernamePasswordAuthenticationToken);
        assertThat(tokenModel.getToken()).isNotBlank();
        assertThat(tokenModel.getJit()).isNotBlank();

    }
}