package com.initgrep.cr.msauth.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Component
@Slf4j
public class KeyUtils {

    @Autowired
    private Environment environment;

    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPath;

    @Value("${access-token.public}")
    private String accessTokenPublicKeyPath;

    @Value("${refresh-token.private}")
    private String refreshTokenPrivateKeyPath;

    @Value("${refresh-token.public}")
    private String refreshTokenPublicKeyPath;

    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(accessTokenKeyPair)) {
            accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return accessTokenKeyPair;
    }

    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(refreshTokenKeyPair)) {
            refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (!publicKeyFile.exists() || !privateKeyFile.exists()) {
            if(Arrays.asList(environment.getActiveProfiles()).contains("prod")){
                throw new RuntimeException("Public or Private key does not exist");
            }
            File directory =  new File("token-keys");
            if(!directory.exists()){
                directory.mkdirs();
            }
            try {
                KeyPairGenerator keyPairGenerator =  KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair generatedKeyPair = keyPairGenerator.generateKeyPair();
                log.info("publicKeyPath = {}",publicKeyPath);
                log.info("privateKeyPath = {} ", privateKeyPath);
                try(FileOutputStream fos =  new FileOutputStream(publicKeyPath)){
                    X509EncodedKeySpec x509EncodedPublicKeySpec = new X509EncodedKeySpec(generatedKeyPair.getPublic().getEncoded());
                    fos.write(x509EncodedPublicKeySpec.getEncoded());
                }

                try(FileOutputStream fos =  new FileOutputStream(privateKeyPath)){
                    PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(generatedKeyPair.getPrivate().getEncoded());
                    fos.write(pKCS8EncodedKeySpec.getEncoded());
                }

            } catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicKeyFileBytes = Files.readAllBytes(publicKeyFile.toPath());
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyFileBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            byte[] privateKeyFileBytes = Files.readAllBytes(privateKeyFile.toPath());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyFileBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            keyPair = new KeyPair(publicKey, privateKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }


}
