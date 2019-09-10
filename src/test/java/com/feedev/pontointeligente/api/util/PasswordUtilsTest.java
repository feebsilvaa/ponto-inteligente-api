package com.feedev.pontointeligente.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.feedev.pontointeligente.api.BaseTest;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class PasswordUtilsTest extends BaseTest {

	private static final String SENHA = "123456";
	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Test
	public void testSenhaNula() {
		assertThat(PasswordUtils.gerarBCrypt(null))
			.isNull();
	}
	
	@Test
	public void testSenhaVazia() {
		assertThat(PasswordUtils.gerarBCrypt(""))
			.isNull();
	}
	
	@Test
	public void testGerarHashSenha() {
		String hashSenha = PasswordUtils.gerarBCrypt(SENHA);
		assertThat(this.bCryptPasswordEncoder.matches(SENHA, hashSenha))
			.isTrue();
	}
	
}
