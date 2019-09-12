package com.feedev.pontointeligente.api.v1.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.feedev.pontointeligente.api.v1.model.entities.Lancamento;
import com.feedev.pontointeligente.api.v1.repository.LancamentoRepository;
import com.feedev.pontointeligente.api.v1.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long id, PageRequest pageRequest) {
		log.info("Buscando lançamentos com paginação de resultados por id: {}", id);
		return this.lancamentoRepository.findByFuncionarioId(id, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando lancamentos por id: {}", id);
		return this.lancamentoRepository.findById(id);
	}

	@Override
	public Lancamento salvarLancamento(Lancamento lancamento) {
		log.info("Salvando lançamento: {}", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void removerLancamento(Long id) {
		log.info("Removendo lancamento com id: {}", id);
		this.lancamentoRepository.deleteById(id);
	}

}
