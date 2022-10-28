package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import com.initgrep.cr.msauth.auth.repository.AppUserTokenRepository;
import com.initgrep.cr.msauth.security.TokenManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.INVALID_APP_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private AppUserTokenRepository tokenRepository;

    @Test
    void test_provideToken_forNotBeingAppUser() {
        assertThat(catchThrowable(
                () -> tokenService
                        .provideToken(UsernamePasswordAuthenticationToken.unauthenticated("principal", null))))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage(INVALID_APP_TOKEN);
    }

    @Test
    void test_provideToken_forNewToken() {
        UserModel user = ServiceTestUtil.getUserModel();
        var authenticationToken = ServiceTestUtil.getMockAuthentication(user);
        var accessTokenModel = new TokenModel("1", "access-token");
        var refreshTokenModel = new TokenModel("2", "refresh-token");
        InternalTokenModel internalTokenModel = new InternalTokenModel(accessTokenModel, refreshTokenModel);
        when(tokenManager.createToken(any(Authentication.class))).thenReturn(internalTokenModel);
        when(tokenRepository.findByJwtId(anyString())).thenReturn(Optional.empty());
        InternalTokenModel resultInternalTokenModel = tokenService.provideToken(authenticationToken);
        assertThat(internalTokenModel).isSameAs(resultInternalTokenModel);
    }

    @Captor
    ArgumentCaptor<AppUserToken> appUserTokenArgumentCaptor;

    @Test
    void test_provideToken_forExistingRefreshToken() {
        UserModel user = ServiceTestUtil.getUserModel();
        var authenticationToken = ServiceTestUtil.getMockAuthentication(user);
        var accessTokenModel = new TokenModel("1", "access-token");
        var refreshTokenModel = new TokenModel("2", "refresh-token");
        InternalTokenModel internalTokenModel = new InternalTokenModel(accessTokenModel, refreshTokenModel);
        when(tokenManager.createToken(any(Authentication.class))).thenReturn(internalTokenModel);
        int hits = 1;
        AppUserToken appUserToken = ServiceTestUtil.getMockAppUserRefreshToken(hits);
        when(tokenRepository.findByJwtId(anyString())).thenReturn(Optional.of(appUserToken));
        InternalTokenModel resultInternalTokenModel = tokenService.provideToken(authenticationToken);
        assertThat(internalTokenModel).isSameAs(resultInternalTokenModel);
        verify(tokenRepository, times(2)).save(appUserTokenArgumentCaptor.capture());
        appUserTokenArgumentCaptor.getAllValues()
                .stream().filter(token -> token.getTokenType().equals(TokenType.REFRESH))
                .findFirst()
                .ifPresent(userToken -> {
                    assertThat(userToken.getHits()).isEqualTo(hits + 1);
                });
    }


}