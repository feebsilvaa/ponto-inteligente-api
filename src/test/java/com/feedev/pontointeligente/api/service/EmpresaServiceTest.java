package com.feedev.pontointeligente.api.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.feedev.pontointeligente.api.BaseTest;
import com.feedev.pontointeligente.api.model.entities.Empresa;
import com.feedev.pontointeligente.api.repository.EmpresaRepository;
import com.feedev.pontointeligente.api.util.CpfCnpjUtil;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class EmpresaServiceTest extends BaseTest {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String CNPJ = CpfCnpjUtil.getCnpj(false);
	
	@Before
	public void setUp() {
		log.info("[Test - {}] - Iniciando setup dos testes.", this.getClass().getSimpleName());
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CNPJ);
		this.empresaRepository.save(empresa);
	}
	
	@After
	public final void tearDown() {
		log.info("[Test - {}] - Finalizando setup dos testes.", this.getClass().getSimpleName());
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarEmpresaPorCnpj() {
		log.info("[Test - {}] - Teste buscando empresa por CNPJ.", this.getClass().getSimpleName());
		Empresa empresaEncontrada = this.empresaService.buscarPorCnpj(CNPJ)
				.map(emp -> emp)
				.orElse(null);
		assertNotNull(empresaEncontrada);		
	}
	
	@Test
	public void testCadastrarNovaEmpresa() {
		log.info("[Test - {}] - Teste cadastrando nova empresa.", this.getClass().getSimpleName());
		Empresa novaEmpresa = new Empresa();
		novaEmpresa.setRazaoSocial("TESTE SALVAR EMPRESA");
		String cnpj = CpfCnpjUtil.getCnpj(false);
		novaEmpresa.setCnpj(cnpj);

		this.empresaService.salvarEmpresa(novaEmpresa);

		Empresa empresaEncontrada = this.empresaService.buscarPorCnpj(cnpj)
				.map(emp -> emp)
				.orElse(null);
		assertNotNull(empresaEncontrada);
	}

}
