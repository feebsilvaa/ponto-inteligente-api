package com.feedev.pontointeligente.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feedev.pontointeligente.api.model.entities.Funcionario;

/*
 * Marcando a interface com Transactional readOnly
 * faz com que todos os métodos da interface sejam somente de leitura
 */
@Transactional(readOnly = true)
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> { 
	
	Funcionario findByCpf(String cgf);
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpfOrEmail(String cpf, String email);

}
