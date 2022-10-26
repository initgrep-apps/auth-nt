package com.initgrep.cr.msauth.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(reason = "Unable to generate key pair")
public class KeyPairGenerationException extends RuntimeException {
    public KeyPairGenerationException(Exception e) {
        super(e);
        log.error("Error while generating Keypair for the token ", e);
    }
}
