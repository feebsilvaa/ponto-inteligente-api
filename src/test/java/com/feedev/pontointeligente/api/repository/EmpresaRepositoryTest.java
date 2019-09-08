package com.feedev.pontointeligente.api.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.feedev.pontointeligente.api.BaseTest;
import com.feedev.pontointeligente.api.model.entities.Empresa;
import com.feedev.pontointeligente.api.util.CpfCnpjUtil;

public class EmpresaRepositoryTest extends BaseTest {

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
	public void testBuscarPorCnpj() {
		Empresa empresaEncontrada = this.empresaRepository.findByCnpj(CNPJ);
		assertEquals(CNPJ, empresaEncontrada.getCnpj());
	}
	
}
