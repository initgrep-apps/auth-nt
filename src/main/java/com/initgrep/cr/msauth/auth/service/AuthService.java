package com.initgrep.cr.msauth.auth.service;

import com.initgrep.cr.msauth.auth.dto.LoginModel;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenModel;

public interface AuthService {

    TokenModel register(RegisterModel registerModel);
    TokenModel login(LoginModel loginModel);
    TokenModel getToken(TokenModel tokenModel);


}
