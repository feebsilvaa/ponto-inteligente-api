package com.feedev.pontointeligente.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PontoInteligenteApiApplicationTests {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void contextLoads() {
		log.info("TEST - {}", this.getClass().getSimpleName());
	}

}
