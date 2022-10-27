package com.initgrep.cr.msauth.auth.providers.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScopeToAuthorityConverterTest {

    @InjectMocks
    ScopeToAuthorityConverter converter;

    @Test
    void testScopeToAuthorityConvert(){
        String scopeString = "X Y";
        Set<SimpleGrantedAuthority> result = converter.convert(scopeString);
        assertNotNull(result);
        assertTrue(result.contains(new SimpleGrantedAuthority("Y")));
        assertTrue(result.contains(new SimpleGrantedAuthority("X")));
    }

}