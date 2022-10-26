package com.initgrep.cr.msauth.security.keypair;

import java.security.KeyPair;

public interface KeyPairVault {

    KeyPair getAccessTokenKeyPair();
    KeyPair getRefreshTokenKeyPair();
}
