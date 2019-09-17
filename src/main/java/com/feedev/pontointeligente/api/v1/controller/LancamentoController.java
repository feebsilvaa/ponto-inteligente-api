package com.feedev.pontointeligente.api.v1.controller;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.model.dto.LancamentoDto;
import com.feedev.pontointeligente.api.v1.model.entities.Lancamento;
import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.service.FuncionarioService;
import com.feedev.pontointeligente.api.v1.service.LancamentoService;

@RestController(value = "v1-lancamento-controller")
@RequestMapping(path = "v1/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping(path = "/funcionario/{funcionarioId}")
	public ResponseEntity<ApiResponse<Page<LancamentoDto>>> listarPorFuncionarioId(
			@PathVariable Long funcionarioId,
			Pageable pageable
			) {
		log.info("Buscando lançamentos por ID do funcionário: {}", funcionarioId);
		ApiResponse<Page<LancamentoDto>> apiResponse = new ApiResponse<Page<LancamentoDto>>();
		
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		
		Page<Lancamento> lancamentosPage = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDtoPage = lancamentosPage.map(l -> this.converterLancamentoParaDto(l));
		
		apiResponse.setData(lancamentosDtoPage);
		
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	private LancamentoDto converterLancamentoParaDto(Lancamento l) {
		LancamentoDto dto = new LancamentoDto();
		dto.setId(Optional.of(l.getId()));
		dto.setData(this.dateFormat.format(l.getDataLancamento()));
		dto.setTipo(l.getTipo().toString());
		dto.setDescricao(l.getDescricao());
		dto.setLocalizacao(l.getLocalizacao());
		dto.setFuncionarioId(l.getFuncionario().getId());
		return dto;
	}

}
