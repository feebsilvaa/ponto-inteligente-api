package com.feedev.pontointeligente.api.service;

import java.util.Optional;

import com.feedev.pontointeligente.api.model.entities.Funcionario;

public interface FuncionarioService {

	Funcionario salvarFuncionario(Funcionario funcionario);

	Optional<Funcionario> buscarPorCpf(String cpf);

	Optional<Funcionario> buscarPorEmail(String email);
	
	Optional<Funcionario> buscarPorId(Long id);
	
}
