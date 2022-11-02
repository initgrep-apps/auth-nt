package com.initgrep.cr.msauth.auth.converter;

import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserToJwtAccessTokenConverterTest {


    @Test
    void convert() throws NoSuchAlgorithmException {
        UserToJwtAccessTokenConverter converter = new UserToJwtAccessTokenConverter(
                new AuthorityToScopeConverter(),
                ConverterTestUtil.getJwtEncoder()
        );
        converter.setAccessTokenExpiryMinutes(5);
        converter.setIssuerApp("testApp");

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