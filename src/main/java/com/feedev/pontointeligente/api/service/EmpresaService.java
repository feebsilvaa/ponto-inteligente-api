package com.feedev.pontointeligente.api.service;

import java.util.Optional;

import com.feedev.pontointeligente.api.model.entities.Empresa;

public interface EmpresaService {

	/**
	 * Retorna uma empresa dado um CNPJ
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa
	 * @param empresa
	 * @return Empresa
	 */
	Empresa salvarEmpresa(Empresa empresa);
}
