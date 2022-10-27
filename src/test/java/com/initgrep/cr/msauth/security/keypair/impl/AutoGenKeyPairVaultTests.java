package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.auth.exception.KeyPairGenerationException;
import com.initgrep.cr.msauth.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AutoGenKeyPairVaultTests {

    @InjectMocks
    private AutoGenKeyPairVault keyPairVault;

    @Mock
    AppConfig appConfig;

    @BeforeEach
    void setup() {
        AppConfig.KeyPairConfig keyPairConfig = new AppConfig.KeyPairConfig();
        keyPairConfig.setDirectory("token-keys-test");
        when(appConfig.getKeyPair()).thenReturn(keyPairConfig);

        AppConfig.AccessTokenConfig accessTokenConfig = new AppConfig.AccessTokenConfig();
        accessTokenConfig.setPublicKeyPath("token-keys-test/access-token-public.key");
        accessTokenConfig.setPrivateKeyPath("token-keys-test/access-token-private.key");
        accessTokenConfig.setExpiryDurationMin(5);

        AppConfig.RefreshTokenConfig refreshTokenConfig = new AppConfig.RefreshTokenConfig();
        refreshTokenConfig.setPublicKeyPath("token-keys-test/refresh-token-public.key");
        refreshTokenConfig.setPrivateKeyPath("token-keys-test/refresh-token-private.key");
        refreshTokenConfig.setExpiryDurationDays(30);
        refreshTokenConfig.setMinRenewalDays(7);

        when(appConfig.getAccessToken()).thenReturn(accessTokenConfig);
        when(appConfig.getRefreshToken()).thenReturn(refreshTokenConfig);


    }

    @AfterEach
    void cleanUp() throws IOException {
        Path parentDirPath = Path.of(appConfig.getKeyPair().getDirectory());
        File parentDir = new File(appConfig.getKeyPair().getDirectory());
        if (Files.isDirectory(parentDirPath) && Files.exists(parentDirPath)) {
            String[] subDirs = parentDir.list();
            if (Objects.nonNull(subDirs)) {
                for (String subPath : subDirs) {
                    Files.delete(parentDirPath.resolve(subPath));
                }
            }
            Files.delete(parentDirPath);
        }

    }

    @Test
    void testGetAccessTokenKeyPair() {
        keyPairVault.createKeyPairsIfNotExist();
        KeyPair accessTokenKeyPair = keyPairVault.getAccessTokenKeyPair();
        assertThat(accessTokenKeyPair).isNotNull();
    }

    @Test
    void testGetAccessTokenKeyPair_forExistingTokenDir() throws IOException {
        Files.createDirectory(Path.of(appConfig.getKeyPair().getDirectory()));
        keyPairVault.createKeyPairsIfNotExist();
        KeyPair accessTokenKeyPair = keyPairVault.getAccessTokenKeyPair();
        assertThat(accessTokenKeyPair).isNotNull();
    }

    @Test
    void testGetAccessTokenKeyPair_forException()  {
        appConfig.getKeyPair().setDirectory("&@@##$$$");
        Assertions.assertThrows(KeyPairGenerationException.class,
                () -> keyPairVault.createKeyPairsIfNotExist());

    }

    @Test
    void testGetAccessTokenKeyPair_forPublicKeyNotExist() throws IOException {
        keyPairVault.createKeyPairsIfNotExist();
        Files.delete(Path.of(appConfig.getAccessToken().getPublicKeyPath()));
        KeyPair accessTokenKeyPair = keyPairVault.getAccessTokenKeyPair();
        assertThat(accessTokenKeyPair).isNotNull();
    }
    @Test
    void testGetAccessTokenKeyPair_forPrivateKeyNotExist() throws IOException {
        keyPairVault.createKeyPairsIfNotExist();
        Files.delete(Path.of(appConfig.getAccessToken().getPrivateKeyPath()));
        KeyPair accessTokenKeyPair = keyPairVault.getAccessTokenKeyPair();
        assertThat(accessTokenKeyPair).isNotNull();
    }
    @Test
    void testGetRefreshTokenKeyPair() {
        keyPairVault.createKeyPairsIfNotExist();
        keyPairVault.createKeyPairsIfNotExist();
        KeyPair refreshTokenKeyPair = keyPairVault.getRefreshTokenKeyPair();
        assertThat(refreshTokenKeyPair).isNotNull();
    }
}
