package com.feedev.pontointeligente.api.v1.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.repository.FuncionarioRepository;
import com.feedev.pontointeligente.api.v1.util.CpfCnpjUtil;
import com.feedev.pontointeligente.api.v1.util.FakerDataUtil;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FuncionarioServiceTest {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@MockBean
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FuncionarioService funcionarioService;
		
	@Before
	public void setUp() {
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
	}
	
	@Test
	public void testSalvarFuncionario() {
		log.info("Teste - Salvar funcinario.");
		Funcionario funcionarioSalvo = this.funcionarioService.salvarFuncionario(new Funcionario());
		assertThat(funcionarioSalvo)
			.isNotNull();
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		log.info("Teste - Buscar funcionario por cpf.");
		Funcionario funcionarioEncontrado = this.funcionarioService.buscarPorCpf(CpfCnpjUtil.getCpf(false))
				.get();
		assertThat(funcionarioEncontrado)
			.isNotNull();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		log.info("Teste - Buscar funcionario por email");
		Funcionario funcionarioEncontrado = this.funcionarioService.buscarPorEmail(new FakerDataUtil().internet().emailAddress())
				.get();
		assertThat(funcionarioEncontrado)
			.isNotNull();
	}
	
	@Test
	public void testBuscarFuncionarioPorId() {
		log.info("Teste - Buscar funcionario por ID.");
		Funcionario funcionarioEncontrado = this.funcionarioService.buscarPorId(1L)
				.get();
		assertThat(funcionarioEncontrado)
			.isNotNull();
	}
}
