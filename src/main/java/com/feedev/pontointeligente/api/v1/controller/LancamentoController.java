package com.feedev.pontointeligente.api.v1.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.model.dto.LancamentoDto;
import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.model.entities.Lancamento;
import com.feedev.pontointeligente.api.v1.model.enums.TipoEnum;
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
	public ResponseEntity<ApiResponse<Page<LancamentoDto>>> listarPorFuncionarioId(@PathVariable Long funcionarioId,
			Pageable pageable) {
		log.info("Buscando lançamentos por ID do funcionário: {}", funcionarioId);
		ApiResponse<Page<LancamentoDto>> apiResponse = new ApiResponse<Page<LancamentoDto>>();

		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

		Page<Lancamento> lancamentosPage = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDtoPage = lancamentosPage.map(l -> this.converterLancamentoParaDto(l));

		apiResponse.setData(lancamentosDtoPage);

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<ApiResponse<LancamentoDto>> listarPorId(@PathVariable Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		ApiResponse<LancamentoDto> apiResponse = new ApiResponse<LancamentoDto>();
		Optional<Lancamento> lancamentoOpt = this.lancamentoService.buscarPorId(id);

		if (!lancamentoOpt.isPresent()) {
			log.info("Lançamento não encontrado para o id: {}", id);
			apiResponse.getErrors().add("Lançamento não encontrado para o id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		}

		apiResponse.setData(this.converterLancamentoParaDto(lancamentoOpt.get()));
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult bindingResult) throws ParseException {
		log.info("Adicionando lançamento: {}", lancamentoDto);
		ApiResponse<LancamentoDto> apiResponse = new ApiResponse<LancamentoDto>();
		validarFuncionario(lancamentoDto, bindingResult);
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, bindingResult);

		if (bindingResult.hasErrors()) {
			log.error("Erro ao validar lançamento: {}", bindingResult.getAllErrors());
			bindingResult.getAllErrors().forEach(error -> apiResponse.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}

		lancamento = this.lancamentoService.salvarLancamento(lancamento);
		apiResponse.setData(this.converterLancamentoParaDto(lancamento));
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<LancamentoDto>> atualizar(
				@PathVariable Long id,
				@Valid @RequestBody LancamentoDto lancamentoDto,
				BindingResult result
			) throws ParseException {
		log.info("Atualizando lançamneto: {}", lancamentoDto.getId());
		
		ApiResponse<LancamentoDto> apiResponse = new ApiResponse<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro ao validar lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> apiResponse.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
		
		lancamento = this.lancamentoService.salvarLancamento(lancamento);
		apiResponse.setData(this.converterLancamentoParaDto(lancamento));
		
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerLancamento(
				@PathVariable Long id
			) {
		log.info("Removendo lancamento com id: {}", id);
		this.lancamentoService.removerLancamento(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private Lancamento converterDtoParaLancamento(@Valid LancamentoDto lancamentoDto, BindingResult bindingResult) throws ParseException {
		Lancamento lancamento = new Lancamento();

		if (lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
			if (lanc.isPresent()) {
				lancamento = lanc.get();
			} else {
				bindingResult.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		} else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setDataLancamento(this.dateFormat.parse(lancamentoDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		} else {
			bindingResult.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return lancamento;
	}

	private void validarFuncionario(@Valid LancamentoDto lancamentoDto, BindingResult bindingResult) {
		if (lancamentoDto.getFuncionarioId() == null) {
			bindingResult.addError(new ObjectError("funcionario", "Funcionario não informado"));
			return;
		}

		log.info("Validando funcionário id: {}", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());

		if (!funcionario.isPresent()) {
			bindingResult.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
		}
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
