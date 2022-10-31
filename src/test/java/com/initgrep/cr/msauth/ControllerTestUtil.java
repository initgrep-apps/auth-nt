package com.initgrep.cr.msauth;

import com.initgrep.cr.msauth.auth.dto.*;

import java.util.Random;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.BEARER;

public class ControllerTestUtil {

    public static final String REGISTER_PATH = "/api/v1/auth/register";
    public static final String LOGIN_PATH = "/api/v1/auth/login";
    public static final String TOKEN_PATH = "/api/v1/auth/token";
    private static final Random rnd = new Random();


    public static RegisterRequest getRegisterRequest() {
        long randomNumber = 10000000000L + ((long) rnd.nextInt(900000000) * 100) + rnd.nextInt(100);
        return RegisterRequest.builder()
                .fullName("test full name")
                .email(randomNumber + "@email.com")
                .phoneNumber("" + randomNumber)
                .password("password")
                .otpInfo(OtpInfoModel.builder()
                        .emailOtp(1111)
                        .phoneOtp(1111)
                        .identifier("ID")
                        .build())
                .build();
    }

    public static LoginRequest getLoginRequest() {
        return LoginRequest.builder()
                .email("test@email.com")
                .password("password")
                .phoneNumber("0000000000")
                .otpInfo(OtpInfoModel.builder()
                        .emailOtp(1111)
                        .phoneOtp(1111)
                        .identifier("ID")
                        .build())
                .build();
    }

    public static RefreshTokenRequest getRefreshTokenRequest() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("refresh-token");
        return refreshTokenRequest;
    }

    public static TokenResponse getTokenResponse() {
        return TokenResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .type(BEARER)
                .expiresIn(300)
                .build();
    }
}
