package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.assertThat;
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
        AppConfig.KeyPairConfig keyPairConfig = KeyPairVaultTestUtil.getKeyPairConfig();
        AppConfig.AccessTokenConfig accessTokenConfig = KeyPairVaultTestUtil.getAccessTokenConfig();
        AppConfig.RefreshTokenConfig refreshTokenConfig = KeyPairVaultTestUtil.getRefreshTokenConfig();

        when(appConfig.getKeyPair()).thenReturn(keyPairConfig);
        lenient().when(appConfig.getAccessToken()).thenReturn(accessTokenConfig);
        lenient().when(appConfig.getRefreshToken()).thenReturn(refreshTokenConfig);

    }

    @AfterEach
    void cleanUp() throws IOException {
        KeyPairVaultTestUtil.cleanTestDirectory(appConfig);
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