package com.initgrep.cr.msauth.auth.dto;

import lombok.Builder;
import lombok.Data;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.BEARER;

@Data
@Builder
public class TokenResponse {
    @Builder.Default
    private String type = BEARER;
    private int expiresIn;
    private String accessToken;
    private String refreshToken;

}
