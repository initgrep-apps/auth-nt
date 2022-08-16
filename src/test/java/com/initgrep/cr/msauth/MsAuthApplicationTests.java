package com.initgrep.cr.msauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@TestPropertySource("classpath:/application-test.properties")
class MsAuthApplicationTests {

	@Test
	void contextLoads() {
	}

}
