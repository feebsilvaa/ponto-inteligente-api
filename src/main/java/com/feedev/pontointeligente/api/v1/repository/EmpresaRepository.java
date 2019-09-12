package com.feedev.pontointeligente.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feedev.pontointeligente.api.v1.model.entities.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	/*
	 * Com a anotação Transactional com o parâmetro
	 * readOnly = true, indicamos ao Hibernate que 
	 * esse método é somente de consulta e não bloqueia o banco para operações
	 */
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
	
}
