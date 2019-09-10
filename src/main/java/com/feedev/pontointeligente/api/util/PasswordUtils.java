package com.feedev.pontointeligente.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
			log.info("{} - Senha vazia ou nula.", PasswordUtils.class.getClass().getSimpleName());
			return null;
		}
		
		log.info("{} - Gerando hash com BCrypt.", PasswordUtils.class.getClass().getName());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
}
