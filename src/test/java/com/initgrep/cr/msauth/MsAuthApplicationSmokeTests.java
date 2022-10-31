package com.initgrep.cr.msauth;

import com.initgrep.cr.msauth.auth.controller.AuthController;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MsAuthApplicationSmokeTests {

    // Add all the controllers here
    @Test
    void smokeTest(@Autowired AuthController authController){
        Assertions.assertThat(authController).isNotNull();
    }

}
