package com.initgrep.cr.msauth.auth.providers.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorityToScopeConverterTest {

    @InjectMocks
    AuthorityToScopeConverter converter;


    @Test
    void scopeConvertTest() {

        String result = converter.convert(Set.of(
                new SimpleGrantedAuthority("X"),
                new SimpleGrantedAuthority("Y")
        ));

        assertEquals("X Y",result);
    }
}