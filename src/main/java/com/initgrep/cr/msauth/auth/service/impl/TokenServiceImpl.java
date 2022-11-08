package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.service.TokenService;
import com.initgrep.cr.msauth.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.INVALID_APP_TOKEN;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenManager tokenManager;

    @Override
    public InternalTokenModel provideToken(Authentication authentication) {
        throwIfNotAppUser(authentication);
        return tokenManager.createToken(authentication);
    }


    private void throwIfNotAppUser(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserModel)) {
            throw new BadCredentialsException(INVALID_APP_TOKEN);
        }
    }
}
