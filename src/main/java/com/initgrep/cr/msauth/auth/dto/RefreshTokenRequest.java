package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

import static com.initgrep.cr.msauth.auth.constants.ValidationConstants.TOKEN_INVALID;

@Data
public class RefreshTokenRequest {
    @NotEmpty(message = TOKEN_INVALID)
    private String refreshToken;
}
