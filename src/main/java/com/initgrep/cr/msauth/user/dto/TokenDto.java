package com.initgrep.cr.msauth.user.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
