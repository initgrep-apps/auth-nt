package com.initgrep.cr.msauth.auth.converter;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testScopeToAuthorityConvertForNull(){
        assertThrows(NullPointerException.class, () -> converter.convert(null));
    }

}