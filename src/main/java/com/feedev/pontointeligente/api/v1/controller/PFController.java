package com.feedev.pontointeligente.api.v1.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.model.dto.PFDto;
import com.feedev.pontointeligente.api.v1.model.entities.Empresa;
import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.model.enums.PerfilEnum;
import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.service.EmpresaService;
import com.feedev.pontointeligente.api.v1.service.FuncionarioService;
import com.feedev.pontointeligente.api.v1.util.PasswordUtils;

@RestController
@RequestMapping("/v1/pf")
@CrossOrigin(origins = "*")
public class PFController {
	
	private final Logger log = LoggerFactory.getLogger(PFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private MessageSource messageSource;
	
	@PostMapping
	public ResponseEntity<ApiResponse<PFDto>> cadastrar(
			@Valid @RequestBody PFDto pfDto,
			BindingResult bindingResult) {
		log.info("Cadastrando PF: {}", pfDto);
		ApiResponse<PFDto> apiResponse = new ApiResponse<PFDto>();
		
		validarDadosExistentes(pfDto, bindingResult);
		
		Funcionario funcionario = this.converterDtoParaFuncionario(pfDto, bindingResult);
		
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação dos dados de cadastro do PF: {}", pfDto);
			bindingResult.getAllErrors().forEach((error) -> {
				String erro = messageSource.getMessage(error, LocaleContextHolder.getLocale());
				log.error("Erro encontrado: {}", erro);
				apiResponse.getErrors()
					.add(erro);
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
		
		// busca empresa e se existir inclui no funcionario
		this.empresaService.buscarPorCnpj(pfDto.getCnpj())
			.ifPresent(emp -> funcionario.setEmpresa(emp));
		Funcionario funcionarioSalvo = this.funcionarioService.salvarFuncionario(funcionario);
		
		apiResponse.setData(this.converterCadastroParaPFDto(funcionarioSalvo));
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	private PFDto converterCadastroParaPFDto(Funcionario funcionarioSalvo) {
		PFDto dto = new PFDto();
		dto.setId(funcionarioSalvo.getId());
		dto.setNome(funcionarioSalvo.getNome());
		dto.setEmail(funcionarioSalvo.getEmail());
		dto.setCpf(funcionarioSalvo.getCpf());
		dto.setCnpj(funcionarioSalvo.getEmpresa().getCnpj());
		funcionarioSalvo.getQtdHorasAlmocoOpt()
			.ifPresent(qtd -> dto.setQdtHorasAlmoco(Optional.of(Double.toString(qtd))));
		funcionarioSalvo.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(qtd -> dto.setQtdHorasTrabalhoDia(Optional.of(Double.toString(qtd))));
		funcionarioSalvo.getValorHoraOpt()
			.ifPresent(valor -> dto.setValorHora(Optional.of(valor.toString())));
		return dto;
	}

	private Funcionario converterDtoParaFuncionario(@Valid PFDto pfDto, BindingResult bindingResult) {
		Funcionario f = new Funcionario();
		f.setNome(pfDto.getNome());
		f.setEmail(pfDto.getEmail());
		f.setCpf(pfDto.getCpf());
		f.setPerfil(PerfilEnum.ROLE_USUARIO);
		f.setSenha(PasswordUtils.gerarBCrypt(pfDto.getSenha()));
		pfDto.getQdtHorasAlmoco()
			.ifPresent(qtd -> f.setQtdHorasAlmoco(Double.valueOf(qtd)));
		pfDto.getQtdHorasTrabalhoDia()
			.ifPresent(qtd -> f.setQtdHorasTrabalhoDia(Double.valueOf(qtd)));
		pfDto.getValorHora()
			.ifPresent(valor -> f.setValorHora(new BigDecimal(valor)));
		return f;
	}

	private void validarDadosExistentes(@Valid PFDto pfDto, BindingResult bindingResult) {
		Optional<Empresa> empresaOpt= this.empresaService.buscarPorCnpj(pfDto.getCnpj());
		if (!empresaOpt.isPresent()) {
			bindingResult.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.funcionarioService.buscarPorCpf(pfDto.getCpf())
			.ifPresent(func -> bindingResult.addError(new ObjectError("funcionario", "CPF já existente")));
		
		this.funcionarioService.buscarPorEmail(pfDto.getEmail())
			.ifPresent(func -> bindingResult.addError(new ObjectError("funcionario", "Email já existente")));
	}
	
}
