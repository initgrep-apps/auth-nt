package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.config.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManualKeyPairVaultTest {

    @InjectMocks
    private ManualKeyPairVault manualKeyPairVault;

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

        lenient().when(appConfig.getAccessToken()).thenReturn(accessTokenConfig);
        lenient().when(appConfig.getRefreshToken()).thenReturn(refreshTokenConfig);

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
    void getAccessTokenKeyPair_forNull() throws IOException {
        Files.createDirectory(Path.of(appConfig.getKeyPair().getDirectory()));
        File accessTokenPublicKeyFile = new File(appConfig.getAccessToken().getPublicKeyPath());
        File accessTokenPrivateKeyFile = new File(appConfig.getAccessToken().getPrivateKeyPath());
        File refreshTokenPublicKeyFile = new File(appConfig.getRefreshToken().getPublicKeyPath());
        File refreshTokenPrivateKeyFile = new File(appConfig.getRefreshToken().getPrivateKeyPath());
        keyPairVault.generateKeyPairIfNotExist(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        keyPairVault.generateKeyPairIfNotExist(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
        manualKeyPairVault.readKeyPair();
        KeyPair accessTokenKeyPair = manualKeyPairVault.getAccessTokenKeyPair();
        assertThat(accessTokenKeyPair).isNotNull();

    }

    @Test
    void getRefreshTokenKeyPair_forNull() throws IOException {
        Files.createDirectory(Path.of(appConfig.getKeyPair().getDirectory()));
        File accessTokenPublicKeyFile = new File(appConfig.getAccessToken().getPublicKeyPath());
        File accessTokenPrivateKeyFile = new File(appConfig.getAccessToken().getPrivateKeyPath());
        File refreshTokenPublicKeyFile = new File(appConfig.getRefreshToken().getPublicKeyPath());
        File refreshTokenPrivateKeyFile = new File(appConfig.getRefreshToken().getPrivateKeyPath());
        keyPairVault.generateKeyPairIfNotExist(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        keyPairVault.generateKeyPairIfNotExist(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
        manualKeyPairVault.readKeyPair();
        KeyPair refreshTokenKeyPair = manualKeyPairVault.getRefreshTokenKeyPair();
        assertThat(refreshTokenKeyPair).isNotNull();

    }

}