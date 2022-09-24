package com.initgrep.cr.msauth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalTokenModel {
    private TokenModel accessToken;
    private TokenModel refreshToken;
}
