package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class TokenServiceTestUtil {

    public static UserModel getUserModel() {
        return UserModel.builder()
                .fullName("user full name")
                .email("test@email.com")
                .build();
    }

    public static UsernamePasswordAuthenticationToken getMockAuthentication(UserModel user) {
        return UsernamePasswordAuthenticationToken.unauthenticated(user, null);
    }

    public static AppUserToken getMockAppUserRefreshToken(int hits) {
        return AppUserToken.builder()
                .tokenType(TokenType.REFRESH)
                .jwtId("2")
                .hits(hits)
                .build();
    }

}
