package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.auth.exception.KeyPairGenerationException;
import com.initgrep.cr.msauth.security.keypair.KeyPairVault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract  class AbstractKeyPairVault implements KeyPairVault {

    protected KeyPair readKeyPairFromPath(File publicKeyFile, File privateKeyFile) {

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
            throw new KeyPairGenerationException(e);
        }

    }
}
