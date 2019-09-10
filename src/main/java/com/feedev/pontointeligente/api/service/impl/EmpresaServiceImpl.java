package com.feedev.pontointeligente.api.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedev.pontointeligente.api.model.entities.Empresa;
import com.feedev.pontointeligente.api.repository.EmpresaRepository;
import com.feedev.pontointeligente.api.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa salvarEmpresa(Empresa empresa) {
		log.info("Salvando empresa: {}", empresa);
		return this.empresaRepository.save(empresa);
	}

}
