package com.initgrep.cr.msauth.auth.service;

import com.initgrep.cr.msauth.auth.dto.InternalTokenModel;
import org.springframework.security.core.Authentication;

public interface TokenService {

    InternalTokenModel provideToken(Authentication authentication);
}
