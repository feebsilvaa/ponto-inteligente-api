package com.feedev.pontointeligente.api.v1.repository;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.feedev.pontointeligente.api.BaseTest;
import com.feedev.pontointeligente.api.v1.model.entities.Empresa;
import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.model.entities.Lancamento;
import com.feedev.pontointeligente.api.v1.model.enums.PerfilEnum;
import com.feedev.pontointeligente.api.v1.model.enums.TipoEnum;
import com.feedev.pontointeligente.api.v1.util.CpfCnpjUtil;
import com.feedev.pontointeligente.api.v1.util.FakerDataUtil;
import com.feedev.pontointeligente.api.v1.util.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest extends BaseTest {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	private Long funcionarioId;
	
	private FakerDataUtil faker;

	@Before
	public void setUp() {
		log.info("[Test - {}] - Iniciando setup dos testes.", this.getClass().getSimpleName());
		this.faker = new FakerDataUtil();
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		
		this.funcionarioId = funcionario.getId();

		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
	}

	@After
	public final void tearDown() {
		log.info("[Test - {}] - Finalizando setup dos testes.", this.getClass().getSimpleName());
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(this.funcionarioId);
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = PageRequest.of(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(this.funcionarioId, page);
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CpfCnpjUtil.getCnpj(false));
		return empresa;
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(faker.name().fullName());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf(CpfCnpjUtil.getCpf(false));
		funcionario.setEmail(this.faker.internet().emailAddress());
		funcionario.setEmpresa(empresa);
		return funcionario;
	}

	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDataLancamento(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		return lancamento;
	}
}
