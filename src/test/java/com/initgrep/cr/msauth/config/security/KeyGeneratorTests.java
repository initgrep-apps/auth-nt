package com.initgrep.cr.msauth.config.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.yaml")
class KeyGeneratorTests {

    @MockBean
    private KeyGenerator keyGenerator;



    @Test
    void testGetAccessTokenKeyPair() {

        KeyPair refreshTokenKeyPair = keyGenerator.getRefreshTokenKeyPair();
        assertThat(refreshTokenKeyPair).isNotNull();
    }

}
