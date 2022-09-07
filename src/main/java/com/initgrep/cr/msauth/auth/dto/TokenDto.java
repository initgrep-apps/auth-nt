package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
