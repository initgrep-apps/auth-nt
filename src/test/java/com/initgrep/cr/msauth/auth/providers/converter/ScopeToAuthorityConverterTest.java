package com.initgrep.cr.msauth.auth.providers.converter;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScopeToAuthorityConverterTest {

    final ScopeToAuthorityConverter converter = new ScopeToAuthorityConverter();

    @Test
    void testScopeToAuthorityConvert(){
        String scopeString = "X Y";
        Set<SimpleGrantedAuthority> result = converter.convert(scopeString);
        assertNotNull(result);
        assertTrue(result.contains(new SimpleGrantedAuthority("Y")));
        assertTrue(result.contains(new SimpleGrantedAuthority("X")));
    }

}