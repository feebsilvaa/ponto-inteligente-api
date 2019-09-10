package com.feedev.pontointeligente.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.feedev.pontointeligente.api.model.entities.Lancamento;

public interface LancamentoService {
	
	Page<Lancamento> buscarPorFuncionarioId(Long id, PageRequest pageRequest);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	Lancamento salvarLancamento(Lancamento lancamento);
	
	void removerLancamento(Long id);
	
}
