package com.initgrep.cr.msauth.auth.service;

import com.initgrep.cr.msauth.auth.dto.*;

public interface AuthService {

    TokenResponse register(RegisterModel registerModel);
    TokenResponse login(LoginModel loginModel);
    TokenResponse getNewAccessToken(RefreshTokenRequest refreshTokenRequest);


}
