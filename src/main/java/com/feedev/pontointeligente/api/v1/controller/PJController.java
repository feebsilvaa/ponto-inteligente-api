package com.feedev.pontointeligente.api.v1.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.model.dto.PJDto;
import com.feedev.pontointeligente.api.v1.model.entities.Empresa;
import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.model.enums.PerfilEnum;
import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.service.EmpresaService;
import com.feedev.pontointeligente.api.v1.service.FuncionarioService;
import com.feedev.pontointeligente.api.v1.util.PasswordUtils;

@RestController(value = "v1-pj-controller")
@RequestMapping(path = "v1/pj")
@CrossOrigin(origins = "*")
public class PJController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public ResponseEntity<ApiResponse<String>> retornarPJs(
			Pageable pageable) {
		ApiResponse<String> response = new ApiResponse<String>();

		
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<PJDto>> cadastrar(
			@Valid @RequestBody PJDto pjDto,
			BindingResult bindingResult) {
		log.info("Cadastrando PJ: {}", pjDto);
		ApiResponse<PJDto> response = new ApiResponse<PJDto>();
		
		validarDadosExistentes(pjDto, bindingResult);
		Empresa empresa = this.converterDtoParaEmpresa(pjDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(pjDto, bindingResult);
		
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação dos dados de cadastro do PJ: {}", pjDto);
			bindingResult.getAllErrors().forEach((error) -> {
				String erro = messageSource.getMessage(error, LocaleContextHolder.getLocale());
				log.error("Erro encontrado: {}", erro);
				response.getErrors()
					.add(erro);
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		this.empresaService.salvarEmpresa(empresa);
		funcionario.setEmpresa(empresa);
		
		this.funcionarioService.salvarFuncionario(funcionario);
		
		response.setData(this.converterParaPJDto(funcionario));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	private PJDto converterParaPJDto(Funcionario funcionario) {
		PJDto pjDto = new PJDto();
		pjDto.setId(funcionario.getId());
		pjDto.setNome(funcionario.getNome());
		pjDto.setEmail(funcionario.getEmail());
		pjDto.setCpf(funcionario.getCpf());
		pjDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		pjDto.setCnpj(funcionario.getEmpresa().getCnpj());
		return pjDto;
	}

	private Funcionario converterDtoParaFuncionario(@Valid PJDto pjDto, BindingResult bindingResult) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(pjDto.getNome());
		funcionario.setEmail(pjDto.getEmail());
		funcionario.setCpf(pjDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(pjDto.getSenha()));
		
		return funcionario;
	}

	private Empresa converterDtoParaEmpresa(@Valid PJDto pjDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(pjDto.getCnpj());
		empresa.setRazaoSocial(pjDto.getRazaoSocial());
		return empresa;
	}

	private void validarDadosExistentes(@Valid PJDto pjDto, BindingResult bindingResult) {
		this.empresaService.buscarPorCnpj(pjDto.getCnpj())
			.ifPresent(emp -> bindingResult.addError(new ObjectError("empresa", "Empresa já existe")));
		
		this.funcionarioService.buscarPorCpf(pjDto.getCpf())
			.ifPresent(func -> bindingResult.addError(new ObjectError("funcionario", "CPF já existe")));
		
		this.funcionarioService.buscarPorEmail(pjDto.getEmail())
			.ifPresent(func -> bindingResult.addError(new ObjectError("funcionario", "Email já existe")));
	}
	
}
