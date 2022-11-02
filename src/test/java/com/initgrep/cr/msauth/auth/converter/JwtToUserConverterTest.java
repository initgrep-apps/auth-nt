package com.initgrep.cr.msauth.auth.converter;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.RESOURCE_SERVER;
import static com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames.SCOPE;
import static org.assertj.core.api.Assertions.*;

class JwtToUserConverterTest {
    JwtToUserConverter converter = new JwtToUserConverter(new ScopeToAuthorityConverter());
    Jwt jwt;


    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        NimbusJwtEncoder jwtEncoder = ConverterTestUtil.getJwtEncoder();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer("test")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .subject("someId")
                .audience(List.of(RESOURCE_SERVER))
                .claim(SCOPE, "X Y")
                .build();

        jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));
    }


    @Test
    void test_JwtToUserConvert() throws NoSuchAlgorithmException {

        UsernamePasswordAuthenticationToken token = converter.convert(jwt);
        assertThat(token).isNotNull();
        Collection<? extends GrantedAuthority> authorities = ((UserModel) token.getPrincipal()).getAuthorities();
        assertThat(authorities.contains(new SimpleGrantedAuthority("X"))).isTrue();
        assertThat(authorities.contains(new SimpleGrantedAuthority("Y"))).isTrue();
        assertThat(token.getCredentials()).isEqualTo(jwt);

    }
}