package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.KeyPair;

@RequiredArgsConstructor
@Component
@Profile({"prod"})
public class ManualKeyPairVault extends AbstractKeyPairVault{

    private final AppConfig appConfig;
    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;


    @Override
    public KeyPair getAccessTokenKeyPair() {
        return accessTokenKeyPair;
    }

    @Override
    public KeyPair getRefreshTokenKeyPair() {
        return refreshTokenKeyPair;
    }

    @PostConstruct
    public void createKeyPairsIfNotExist() {
        File accessTokenPublicKeyFile = new File(appConfig.getAccessToken().getPublicKeyPath());
        File accessTokenPrivateKeyFile = new File(appConfig.getAccessToken().getPrivateKeyPath());
        accessTokenKeyPair = readKeyPairFromPath(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);

        File refreshTokenPublicKeyFile = new File(appConfig.getRefreshToken().getPublicKeyPath());
        File refreshTokenPrivateKeyFile = new File(appConfig.getRefreshToken().getPublicKeyPath());
        refreshTokenKeyPair = readKeyPairFromPath(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
    }


}
