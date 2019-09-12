package com.feedev.pontointeligente.api.v1.util;

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
		
		// Regex - ^\d{3}\.\d{3}\.\d{3}\-\d{2}$
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
	
	@Test
	public void testGerarCnpjSemPontuação() {
		log.info("Teste - Gerar CNPJ sem pontuação.");
		String cnpj = CpfCnpjUtil.getCnpj(false);
		assertThat(cnpj)
			.matches("[0-9]{14}");
	}
	
	@Test
	public void testGerarCnpjComPontuação() {
		log.info("Teste - Gerar CNPJ com pontuação.");
		String cnpj = CpfCnpjUtil.getCnpj(true);
		
		// Regex - ^\d{2}\.\d{3}\.\d{3}\/\d{4}\-\d{2}$
		assertThat(cnpj)
			.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$");
	}
	
	@Test
	public void testVerificarCnpjValido() {
		log.info("Teste - Verificar CNPJ válido.");
		String cnpj = CpfCnpjUtil.getCnpj(true);
		assertThat(new CpfCnpjUtil().isCNPJ(cnpj))
			.isTrue();
	}
	
	@Test
	public void testVerificarCnpjInvalido() {
		log.info("Teste - Verificar CNPJ inválido.");
		assertThat(new CpfCnpjUtil().isCNPJ("23.223.332/2223-00"))
			.isFalse();
	}
	
	@Test
	public void testGerarRgSemPontuacao() {
		log.info("Teste - Gerar RG sem pontuação.");
		String rg = CpfCnpjUtil.getRg(false);
		assertThat(rg)
			.hasSize(9);
	}
	
	@Test
	public void testGerarRgComPontuacao() {
		log.info("Teste - Gerar RG com pontuação.");
		String rg = CpfCnpjUtil.getRg(true);
		assertThat(rg)
			.hasSize(12);
	}
	
}
