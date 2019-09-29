package com.feedev.pontointeligente.api.v1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);
	
	public PasswordUtils() {
	}

	/**
	 * Gera um hash utilizando o Bcrypt.
	 * 
	 * @param senha
	 * @return
	 */
	public static String gerarBCrypt(String senha) {
		if (StringUtils.isEmpty(senha)) {
			log.info("Senha vazia ou nula.");
			return null;
		}
		
		log.info("Gerando hash com BCrypt.");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
	
	public static void main(String[] args) {
		String pass = "123456";
		String hash = PasswordUtils.gerarBCrypt(pass);
		log.info("{}", hash);
		log.info("{}", BCrypt.checkpw(pass, hash));
	}
}
