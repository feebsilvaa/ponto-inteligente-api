package com.feedev.pontointeligente.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PontoInteligenteApiApplication {
	
	private static Logger log = LoggerFactory.getLogger(PontoInteligenteApiApplication.class);
	
	public static void main(String[] args) {	
		SpringApplication.run(PontoInteligenteApiApplication.class, args);
		log.info("FEEDEV - It Works {}", log.isDebugEnabled());
	}

}
