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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.exception.FuncionarioNaoExisteException;
import com.feedev.pontointeligente.api.v1.model.dto.FuncionarioRequestDto;
import com.feedev.pontointeligente.api.v1.model.dto.FuncionarioResponseDto;
import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.service.FuncionarioService;
import com.feedev.pontointeligente.api.v1.util.PasswordUtils;

@RestController(value = "v1-funcionario-controller")
@RequestMapping(path = "v1/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private MessageSource messageSource;
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<ApiResponse<FuncionarioResponseDto>> atualizar(
				@PathVariable("id") Long id,
				@Valid @RequestBody FuncionarioRequestDto funcDto,
				BindingResult bindingResult
			) throws FuncionarioNaoExisteException {
		log.info("Atualizando funcionário: {}", funcDto);
		ApiResponse<FuncionarioResponseDto> apiResponse = new ApiResponse<FuncionarioResponseDto>();
		
		Optional<Funcionario> func = this.funcionarioService.buscarPorId(id);
		if (!func.isPresent()) {
			bindingResult.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
		}
		
		// validação incluida para caso não tenha encontrado o usuário, não tente atualizar na tabela
		if (!bindingResult.hasErrors()) {			
			this.atualizarDadosFuncionarios(func.get(), funcDto, bindingResult);
		}
		
		
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação dos dados de Funcionário: {}", funcDto);
			bindingResult.getAllErrors().forEach((error) -> {
				String erro = messageSource.getMessage(error, LocaleContextHolder.getLocale());
				log.error("Erro encontrado: {}", erro);
				apiResponse.getErrors()
					.add(erro);
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
		
		Funcionario funcionarioSalvo = this.funcionarioService.salvarFuncionario(func.get());
		apiResponse.setData(this.converterFuncionarioParaDto(funcionarioSalvo));
				
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	

	private FuncionarioResponseDto converterFuncionarioParaDto(Funcionario funcionarioSalvo) {
		FuncionarioResponseDto funcionarioDto = new FuncionarioResponseDto();
		funcionarioDto.setId(funcionarioSalvo.getId());
		funcionarioDto.setEmail(funcionarioSalvo.getEmail());
		funcionarioDto.setNome(funcionarioSalvo.getNome());
		funcionarioSalvo.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Double.toString(qtdHorasAlmoco))));
		funcionarioSalvo.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Double.toString(qtdHorasTrabDia))));
		funcionarioSalvo.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

		return funcionarioDto;
	}

	private void atualizarDadosFuncionarios(Funcionario funcionario, @Valid FuncionarioRequestDto funcDto,
			BindingResult bindingResult) {
		funcionario.setNome(funcDto.getNome());

		if (!funcionario.getEmail().equals(funcDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcDto.getEmail())
					.ifPresent(func -> bindingResult.addError(new ObjectError("email", "Email já existente.")));
			funcionario.setEmail(funcDto.getEmail());
		}

		funcionario.setQtdHorasAlmoco(null);
		funcDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Double.valueOf(qtdHorasAlmoco)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Double.valueOf(qtdHorasTrabDia)));

		funcionario.setValorHora(null);
		funcDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if (funcDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcDto.getSenha().get()));
		}
		
	}
	
}
