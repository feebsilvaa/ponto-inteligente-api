package com.feedev.pontointeligente.api.repository;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feedev.pontointeligente.api.model.entities.Lancamento;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "LancamentoRepository.findByFuncionarioId",
			query = ""
					+ "SELECT lanc "
					+ "FROM Lancamento lanc "
					+ "WHERE lanc.funcionario.id = :funcionarioId")
})
@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

	List<Lancamento> findByFuncionarioId(@Param("funcionario") Long funcionarioId);

	Page<Lancamento> findByFuncionarioId(@Param("funcionario") Long funcionarioId, Pageable pageable);
		
}
