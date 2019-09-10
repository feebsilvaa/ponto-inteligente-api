package com.feedev.pontointeligente.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.feedev.pontointeligente.api.model.entities.Funcionario;
import com.feedev.pontointeligente.api.model.enums.PerfilEnum;
import com.feedev.pontointeligente.api.util.CpfCnpjUtil;
import com.feedev.pontointeligente.api.util.FakerDataUtil;
import com.feedev.pontointeligente.api.util.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest extends BaseTest {
	
	@Autowired 
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private FakerDataUtil faker;
	
	private static final String EMAIL = new FakerDataUtil().internet().emailAddress();
	private static final String CPF = CpfCnpjUtil.getCpf(false);
		
	@Before
	public void setUp() {
		log.info("[Test - {}] - Iniciando setup dos testes.", this.getClass().getSimpleName());
		this.faker = new FakerDataUtil();		
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		this.funcionarioRepository.save(obterDadosFuncionario(empresa));
	}

	@After
	public final void tearDown() {
		log.info("[Test - {}] - Finalizando setup dos testes.", this.getClass().getSimpleName());
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		assertEquals(CPF, funcionario.getCpf());
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailOuCpfInformandoCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, null);
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailOuCpfInformandoEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(null, EMAIL);
		assertNotNull(funcionario);
	}
	
	@Test
	public void testTentarBuscarFuncionarioPorEmailNaoCadastrado() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(this.faker.internet().emailAddress());
		assertNull(funcionario);
	}
	
	@Test
	public void testTentarBuscarFuncionarioPorEmailNulo() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(null);
		assertNull(funcionario);
	}
	
	@Test
	public void testTentarBuscarFuncionarioPorCpfNaoCadastrado() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CpfCnpjUtil.getCpf(false));
		assertNull(funcionario);
	}
	
	@Test
	public void testTentarBuscarFuncionarioPorCpfNulo() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(null);
		assertNull(funcionario);
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
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
	
}
