package com.initgrep.cr.msauth.auth.providers.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.SPACE;

@Component
public class AuthorityToScopeConverter implements Converter<Set<SimpleGrantedAuthority>, String> {
    @Override
    public String convert(@NonNull Set<SimpleGrantedAuthority> authorities) {
        return
                authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.joining(SPACE));
    }
}
