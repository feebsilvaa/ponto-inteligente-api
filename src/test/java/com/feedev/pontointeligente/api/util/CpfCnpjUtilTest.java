package com.feedev.pontointeligente.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class CpfCnpjUtilTest {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testGerarCpfSemPontuacao() {
		log.info("Teste - Gerar cpf sem pontuação.");
		String cpf = CpfCnpjUtil.getCpf(false);
		assertThat(cpf)
			.matches("[0-9]{11}");
	}

	@Test
	public void testGerarCpfComPontuacao() {
		log.info("Teste - Gerar cpf com pontuação.");
		String cpf = CpfCnpjUtil.getCpf(true);
		assertThat(cpf)
			.matches("^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$");
	}
	
	@Test
	public void testVerificarCpfValido() {
		log.info("Teste - Verificar cpf valido.");
		String cpf = CpfCnpjUtil.getCpf(false);
		assertThat(new CpfCnpjUtil().isCPF(cpf))
			.isTrue();
	}
	
	@Test
	public void testVerificarCpfInvalido() {
		log.info("Teste - Verificar cpf invalido.");
		assertThat(new CpfCnpjUtil().isCPF("39940592230"))
			.isFalse();
	}
	
}
