package com.feedev.pontointeligente.api.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.model.dto.PJDto;
import com.feedev.pontointeligente.api.model.entities.Empresa;
import com.feedev.pontointeligente.api.model.entities.Funcionario;
import com.feedev.pontointeligente.api.response.ApiResponse;
import com.feedev.pontointeligente.api.service.EmpresaService;
import com.feedev.pontointeligente.api.service.FuncionarioService;

@RestController(value = "v1-pj-controller")
@RequestMapping("/v1/pj")
@CrossOrigin(origins = "*")
public class PJController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public PJController() {
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
			log.error("Erro de validação dos dados de cadastro do PJ: {}", bindingResult.getAllErrors());			
			bindingResult.getAllErrors().forEach((error) -> {
				response.getErrors()
					.add(error.getDefaultMessage());
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		this.empresaService.salvarEmpresa(empresa);
		funcionario.setEmpresa(empresa);
		
		this.funcionarioService.salvarFuncionario(funcionario);
		
		response.setData(this.converterParaPJDto(funcionario));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
