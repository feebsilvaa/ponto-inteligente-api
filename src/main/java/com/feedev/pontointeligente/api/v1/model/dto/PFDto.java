package com.feedev.pontointeligente.api.v1.model.dto;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class PFDto {

	private Long id;

	@NotEmpty
	@Length(min = 3, max = 200)
	private String nome;

	@NotEmpty
	@Length(min = 5, max = 200)
	private String email;

	@NotEmpty
	private String senha;

	@NotEmpty
	@CPF
	private String cpf;

	private Optional<String> valorHora = Optional.empty();
	private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
	private Optional<String> qdtHorasAlmoco = Optional.empty();

	@NotEmpty
	@CNPJ
	private String cnpj;

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public Optional<String> getQdtHorasAlmoco() {
		return qdtHorasAlmoco;
	}

	public void setQdtHorasAlmoco(Optional<String> qdtHorasAlmoco) {
		this.qdtHorasAlmoco = qdtHorasAlmoco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "PFDto [" + " id=" + id + " nome=" + nome + " email=" + email + " senha=" + senha + " cpf=" + cpf
				+ " valorHora=" + valorHora + " qtdHorasTrabalhoDia=" + qtdHorasTrabalhoDia + " qtdHorasAlmoco="
				+ qdtHorasAlmoco + " cnpj=" + cnpj + "]";
	}

}
