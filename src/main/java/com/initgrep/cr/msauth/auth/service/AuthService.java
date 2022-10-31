package com.initgrep.cr.msauth.auth.service;

import com.initgrep.cr.msauth.auth.dto.*;

public interface AuthService {

    TokenResponse register(RegisterRequest registerRequest);
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse getNewAccessToken(RefreshTokenRequest refreshTokenRequest);


}
