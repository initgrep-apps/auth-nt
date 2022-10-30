package com.initgrep.cr.msauth;

import com.initgrep.cr.msauth.auth.dto.OtpInfoModel;
import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class HttpRequestTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final Random rnd =  new Random();

    @Test
    void registerApiShouldReturnAResponse(@LocalServerPort int port) {
        String localhost = "http://0.0.0.0:";
        var url = localhost + port + "/api/v1/auth/register";
        long randomNumber = 10000000000L + ((long)rnd.nextInt(900000000)*100) + rnd.nextInt(100);
        var registerModel = new RegisterModel();
        registerModel.setFullName("test full name");
        registerModel.setEmail(randomNumber+"@email.com");
        registerModel.setPhoneNumber(""+randomNumber);
        registerModel.setPassword("password");
        registerModel.setOtpInfo(new OtpInfoModel());
        registerModel.getOtpInfo().setEmailOtp(11111);
        registerModel.getOtpInfo().setPhoneOtp(11111);
        registerModel.getOtpInfo().setIdentifier(UUID.randomUUID().toString());
        ResponseEntity<TokenResponse> tokenResponseEntity
                = testRestTemplate.postForEntity(url, registerModel, TokenResponse.class);

        log.info("tokenResponse  = {}", tokenResponseEntity.getBody());
        assertThat(tokenResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(tokenResponseEntity.getBody()).isNotNull();
        assertThat(tokenResponseEntity.getBody().getAccessToken()).isNotNull();
        assertThat(tokenResponseEntity.getBody().getRefreshToken()).isNotNull();
    }
}
