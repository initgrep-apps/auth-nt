package com.initgrep.cr.msauth.auth.aspect;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import com.initgrep.cr.msauth.auth.repository.AppUserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Configuration
public class AuthTokenAnalysisAspect {
    private final AppUserTokenRepository tokenRepository;

    @Pointcut("execution( public com.initgrep.cr.msauth.auth.dto.InternalTokenModel com.initgrep.cr.msauth.auth.service.TokenService.provideToken(..))")
    public void atProvideToken() {
        // does not require body
    }

    @AfterReturning(pointcut = "atProvideToken()", returning = "internalTokenModel")
    public void saveTokenAfterReturnOfProvideToken(JoinPoint joinPoint, InternalTokenModel internalTokenModel) {
        saveToken(internalTokenModel.getAccessToken(), TokenType.ACCESS);
        saveToken(internalTokenModel.getRefreshToken(), TokenType.REFRESH);
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

}
