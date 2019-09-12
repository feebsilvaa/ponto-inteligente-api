package com.feedev.pontointeligente.api.v1.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedev.pontointeligente.api.v1.model.entities.Funcionario;
import com.feedev.pontointeligente.api.v1.repository.FuncionarioRepository;
import com.feedev.pontointeligente.api.v1.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Override
	public Funcionario salvarFuncionario(Funcionario funcionario) {
		log.info("Salvando funcionario: {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buscando funcionario por cpf: {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando funcionario por e-mail: {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscando funcionario por id: {}", id);
		return this.funcionarioRepository.findById(id);
	}

}
