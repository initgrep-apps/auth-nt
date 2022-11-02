package com.initgrep.cr.msauth;

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

import static com.initgrep.cr.msauth.ControllerTestUtil.REGISTER_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class HttpRequestTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final Random rnd = new Random();

    @Test
    void registerApiShouldReturnAResponse(@LocalServerPort int port) {
        String localhost = "http://0.0.0.0:";
        var url = localhost + port + REGISTER_PATH;
        var registerModel = ControllerTestUtil.getRegisterRequest();
        ResponseEntity<TokenResponse> tokenResponseEntity
                = testRestTemplate.postForEntity(url, registerModel, TokenResponse.class);
        assertThat(tokenResponseEntity).isNotNull()
                .satisfies(tp -> {
                    assertThat(tp.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                    assertThat(tp.getBody()).isNotNull();
                    assertThat(tp.getBody().getAccessToken()).isNotNull();
                    assertThat(tp.getBody().getRefreshToken()).isNotNull();
                });

    }
}
