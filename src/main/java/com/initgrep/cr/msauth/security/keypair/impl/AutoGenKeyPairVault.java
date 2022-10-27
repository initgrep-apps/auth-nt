package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.auth.exception.KeyPairGenerationException;
import com.initgrep.cr.msauth.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@RequiredArgsConstructor
@Slf4j
@Component
@Profile({"dev", "test"})
class AutoGenKeyPairVault extends AbstractKeyPairVault {

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
        log.debug("KeyPairConfig :: {}", appConfig);
        File accessTokenPublicKeyFile = new File(appConfig.getAccessToken().getPublicKeyPath());
        File accessTokenPrivateKeyFile = new File(appConfig.getAccessToken().getPrivateKeyPath());
        File refreshTokenPublicKeyFile = new File(appConfig.getRefreshToken().getPublicKeyPath());
        File refreshTokenPrivateKeyFile = new File(appConfig.getRefreshToken().getPrivateKeyPath());
        createTokenDirectoryIfNotExist();
        generateKeyPairIfNotExist(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        generateKeyPairIfNotExist(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
        accessTokenKeyPair = readKeyPairFromPath(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        refreshTokenKeyPair = readKeyPairFromPath(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
    }

    private void createTokenDirectoryIfNotExist() {
        File keyDirectory = new File(appConfig.getKeyPair().getDirectory());
        if (!keyDirectory.exists()) {
            log.debug("keypair dir does not exist");
            boolean created = keyDirectory.mkdirs();
            log.debug("KeyPair directory {} were created ? -- [{}] ", keyDirectory.getAbsoluteFile(), created);
        }
    }


    private void generateKeyPairIfNotExist(File publicKeyFile, File privateKeyFile) {
        if ( publicKeyFile.exists() && privateKeyFile.exists()) {
            log.debug("KEY_PAIR_ALREADY_EXIST");
            return;
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair generatedKeyPair = keyPairGenerator.generateKeyPair();
            log.debug("publicKeyPath = {}", publicKeyFile.toPath());
            log.debug("privateKeyPath = {} ", privateKeyFile.toPath());
            try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
                X509EncodedKeySpec x509EncodedPublicKeySpec = new X509EncodedKeySpec(generatedKeyPair.getPublic().getEncoded());
                fos.write(x509EncodedPublicKeySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(generatedKeyPair.getPrivate().getEncoded());
                fos.write(pKCS8EncodedKeySpec.getEncoded());
            }

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new KeyPairGenerationException(e);
        }
    }
}

