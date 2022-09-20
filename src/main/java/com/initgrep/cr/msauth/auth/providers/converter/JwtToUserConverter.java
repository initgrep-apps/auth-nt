package com.initgrep.cr.msauth.auth.providers.converter;

import com.initgrep.cr.msauth.auth.constants.JwtExtendedClaimNames;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    private ScopeToAuthorityConverter scopeToAuthorityConverter;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        
        var userModel = UserModel.builder()
                .identifier(jwt.getSubject())
                .grantedAuthorities(scopeToAuthorityConverter.convert(jwt.getClaim(JwtExtendedClaimNames.SCOPE)))
                .build();
        return new UsernamePasswordAuthenticationToken(userModel, jwt, Collections.emptyList());
    }


}
