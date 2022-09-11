package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class TokenModel {
    private String accessToken;
    private String refreshToken;
}
