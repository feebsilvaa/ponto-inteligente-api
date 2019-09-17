package com.feedev.pontointeligente.api.v1.model.dto;

import java.util.Optional;

public class FuncionarioResponseDto {

	private Long id;
	
	private String nome;
	
	private String email;
	
	private Optional<String> valorHora = Optional.empty();
	
	private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
	
	private Optional<String> qtdHorasAlmoco = Optional.empty();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Optional<String> getValorHora() {
		return valorHora;
	}

	public void setValorHora(Optional<String> valorHora) {
		this.valorHora = valorHora;
	}

	public Optional<String> getQtdHorasTrabalhoDia() {
		return qtdHorasTrabalhoDia;
	}

	public void setQtdHorasTrabalhoDia(Optional<String> qtdHorasTrabalhoDia) {
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}

	public Optional<String> getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}

	public void setQtdHorasAlmoco(Optional<String> qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}
	
	@Override
	public String toString() {
		return "FuncionarioResponseDto ["
				+ " id=" + id
				+ " nome=" + nome
				+ " email=" + email
				+ " valorHora=" + valorHora
				+ " qtdHorasTrabalhoDia=" + qtdHorasTrabalhoDia
				+ " qtdHorasAlmoco=" + qtdHorasAlmoco
				+ "]";
	}

}
