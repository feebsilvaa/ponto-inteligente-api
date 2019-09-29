package com.feedev.pontointeligente.api.v1.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.feedev.pontointeligente.api.v1.model.entities.Empresa;
import com.feedev.pontointeligente.api.v1.service.EmpresaService;
import com.feedev.pontointeligente.api.v1.util.CpfCnpjUtil;
import com.feedev.pontointeligente.api.v1.util.FakerDataUtil;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EmpresaControllerTest {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmpresaService empresaService;
	
	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/v1/empresa/cnpj/";
	private static final Long ID = Long.valueOf(1L);
	private static final String CNPJ = CpfCnpjUtil.getCnpj(false);
	private static final String RAZAO_SOCIAL = new FakerDataUtil().company().name();

	@Test
	@WithMockUser
	public void testTentarBuscarEmpresaPorCNPJInexistente() throws Exception {
		log.info("Teste - Buscando empresa por cnpj inexistente: {}", CNPJ);
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
	
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
					.accept(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ));
	}
	
	@Test
	@WithMockUser
	public void testBuscarEmpresaPorCNPJ() throws Exception {
		log.info("Teste - Buscando empresa por cnpj: {}", CNPJ);
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString()))
			.willReturn(Optional.of(this.obterDadosEmpresa()));
	
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
					.accept(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.data.id").value(ID))
					.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
					.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
					.andExpect(jsonPath("$.errors").isEmpty());
	}

	private Empresa obterDadosEmpresa() {
		Empresa e = new Empresa();
		e.setId(ID);
		e.setRazaoSocial(RAZAO_SOCIAL);
		e.setCnpj(CNPJ);
		return e;
	}

}
