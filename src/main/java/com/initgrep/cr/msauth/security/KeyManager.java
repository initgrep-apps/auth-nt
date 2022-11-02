package com.initgrep.cr.msauth.security;

import com.initgrep.cr.msauth.security.keypair.KeyPairVault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@RequiredArgsConstructor
@Slf4j
@Component
public class KeyManager {

    private final KeyPairVault keyPairVault;

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) keyPairVault.getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) keyPairVault.getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) keyPairVault.getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) keyPairVault.getRefreshTokenKeyPair().getPrivate();
    }

}
