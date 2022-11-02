package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import com.initgrep.cr.msauth.auth.repository.AppUserTokenRepository;
import com.initgrep.cr.msauth.auth.service.TokenService;
import com.initgrep.cr.msauth.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.INVALID_APP_TOKEN;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenManager tokenManager;

    private final AppUserTokenRepository tokenRepository;

    @Override
    public InternalTokenModel provideToken(Authentication authentication) {
        throwIfNotAppUser(authentication);
        InternalTokenModel internalTokenModel = tokenManager.createToken(authentication);
        saveToken(internalTokenModel.getAccessToken(), TokenType.ACCESS);
        saveToken(internalTokenModel.getRefreshToken(), TokenType.REFRESH);
        return internalTokenModel;
    }

    private void saveToken(TokenModel tokenModel, TokenType tokenType) {
        var userToken = AppUserToken.builder()
                .jwtId(tokenModel.getJit())
                .tokenType(tokenType)
                .build();

        if (tokenType.equals(TokenType.REFRESH)) {
            Optional<AppUserToken> tokenByJwtId = tokenRepository.findByJwtId(tokenModel.getJit());
            if (tokenByJwtId.isPresent()) {
                userToken = tokenByJwtId.get();
                userToken.setHits(userToken.getHits() + 1);
            }
        }
        tokenRepository.save(userToken);
    }


    private void throwIfNotAppUser(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserModel)) {
            throw new BadCredentialsException(INVALID_APP_TOKEN);
        }
    }
}
