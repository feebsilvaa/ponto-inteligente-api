package com.feedev.pontointeligente.api.v1.controller;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.model.dto.EmpresaDto;
import com.feedev.pontointeligente.api.v1.model.entities.Empresa;
import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.service.EmpresaService;

@RestController(value = "v1-empresa-controller")
@RequestMapping(path = "v1/empresa")
@CrossOrigin("*")
public class EmpresaController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmpresaService empresaService;

	@GetMapping(path = "cnpj/{cnpj}")
	public ResponseEntity<?> buscarEmpresaPorCnpj(@PathVariable("cnpj") String cnpj) {
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		Optional<Empresa> empresaOpt = empresaService.buscarPorCnpj(cnpj);

		if (!empresaOpt.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(null,
					HttpStatus.BAD_REQUEST.name(), Arrays.asList("Empresa não encontrada para o CNPJ " + cnpj)));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse<>(this.converterEmpresaParaDto(empresaOpt.get()), HttpStatus.OK.name(), null));
	}

	private EmpresaDto converterEmpresaParaDto(Empresa empresa) {
		EmpresaDto dto = new EmpresaDto();
		dto.setId(empresa.getId());
		dto.setCnpj(empresa.getCnpj());
		dto.setRazaoSocial(empresa.getRazaoSocial());
		return dto;
	}

}
