package com.initgrep.cr.msauth.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class KeyGenerator {

    private final Environment environment;

    @Value("${app.access-token.private-path}")
    private String accessTokenPrivateKeyPath;

    @Value("${app.access-token.public-path}")
    private String accessTokenPublicKeyPath;

    @Value("${app.refresh-token.private-path}")
    private String refreshTokenPrivateKeyPath;

    @Value("${app.refresh-token.public-path}")
    private String refreshTokenPublicKeyPath;

    @Value("${app.key-pair.dir}")
    private String keyPairDir;
    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;

    KeyPair getAccessTokenKeyPair() {
        return accessTokenKeyPair;
    }

    KeyPair getRefreshTokenKeyPair() {
        return refreshTokenKeyPair;
    }


    @PostConstruct
    public void createKeyPairsIfNotExist() {
        File accessTokenPublicKeyFile = new File(accessTokenPublicKeyPath);
        File accessTokenPrivateKeyFile = new File(accessTokenPrivateKeyPath);
        File refreshTokenPublicKeyFile = new File(refreshTokenPublicKeyPath);
        File refreshTokenPrivateKeyFile = new File(refreshTokenPrivateKeyPath);

        File directory = new File(keyPairDir);
        if (!directory.exists()) {
            log.debug("keypair dir does not exist");
            boolean created = directory.mkdirs();
            if (!created) {
                log.debug("unable to create keypair  dir:: {} ", keyPairDir);
            } else {
                log.debug("keyPair dir created successfully");
            }

        }
        generateKeyPair(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        generateKeyPair(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
        accessTokenKeyPair = assignKeyPair(accessTokenPublicKeyFile, accessTokenPrivateKeyFile);
        refreshTokenKeyPair = assignKeyPair(refreshTokenPublicKeyFile, refreshTokenPrivateKeyFile);
    }


    private KeyPair assignKeyPair(File publicKeyFile, File privateKeyFile) {

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicKeyFileBytes = Files.readAllBytes(publicKeyFile.toPath());
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyFileBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            byte[] privateKeyFileBytes = Files.readAllBytes(privateKeyFile.toPath());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyFileBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            return new KeyPair(publicKey, privateKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateKeyPair(File publicKeyFile, File privateKeyFile) {
        boolean keyPairExist = publicKeyFile.exists() && privateKeyFile.exists();
        boolean isProdEnv = List.of(environment.getActiveProfiles()).contains("prod");

        if (keyPairExist || isProdEnv) {
            log.debug("Key pair can not be generated for reasons :: prod env or key pair already exist");
            return;
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair generatedKeyPair = keyPairGenerator.generateKeyPair();
            log.info("publicKeyPath = {}", publicKeyFile.toPath().toString());
            log.info("privateKeyPath = {} ", privateKeyFile.toPath().toString());
            try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
                X509EncodedKeySpec x509EncodedPublicKeySpec = new X509EncodedKeySpec(generatedKeyPair.getPublic().getEncoded());
                fos.write(x509EncodedPublicKeySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(generatedKeyPair.getPrivate().getEncoded());
                fos.write(pKCS8EncodedKeySpec.getEncoded());
            }

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
