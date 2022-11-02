package com.initgrep.cr.msauth.auth.converter;


import com.initgrep.cr.msauth.auth.constants.AuthConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScopeToAuthorityConverter implements Converter<String, Set<SimpleGrantedAuthority>> {

    @Override
    public Set<SimpleGrantedAuthority> convert(@NonNull String  scopeItems) {
        return Arrays.stream(scopeItems.split(AuthConstants.SPACE)).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
