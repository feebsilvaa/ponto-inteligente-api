package com.feedev.pontointeligente.api.v1.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.feedev.pontointeligente.api.v1.model.entities.Lancamento;
import com.feedev.pontointeligente.api.v1.repository.LancamentoRepository;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LancamentoServiceTest {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Before
	public void setUp() {
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
			.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findById(Mockito.anyLong()))
			.willReturn(Optional.ofNullable(new Lancamento()));
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class)))
			.willReturn(new Lancamento());
	}
	
	@Test
	public void testBuscarLancamentoPorIdDoFuncionarioPaginado() {
		log.info("Teste - Buscar Lancamentos paginados por id do funcionario.");
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(1L, PageRequest.of(0, 10));
		assertThat(lancamentos)
			.isNotNull();
	}
	
	@Test
	public void testBuscarPorId() {
		log.info("Teste - Buscar lancamento por id");
		Lancamento lancamento = this.lancamentoService.buscarPorId(1L).get();
		assertThat(lancamento)
			.isNotNull();
	}
	
	@Test
	public void testSalvarLancamento() {
		log.info("Teste - Salvar lancamento.");
		Lancamento lancamentoSalvo = this.lancamentoService.salvarLancamento(new Lancamento());
		assertThat(lancamentoSalvo)
			.isNotNull();
	}
	
}
