package com.initgrep.cr.msauth.auth.providers.converter;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorityToScopeConverterTest {

    final AuthorityToScopeConverter converter = new AuthorityToScopeConverter();


    @Test
    void scopeConvertTest() {

        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority("X"));
        authorities.add(new SimpleGrantedAuthority("Y"));
        String result = converter.convert(authorities);
        assertEquals("X Y", result);
    }
}