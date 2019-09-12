package com.feedev.pontointeligente.api.v1.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class PJDto {

	private Long id;
	
	@NotEmpty
	@Length(min = 3, max = 200)
	private String nome;
	
	@NotEmpty
	@Length(min = 5, max = 200)
	@Email
	private String email;
	
	@NotEmpty
	private String senha;
	
	@NotEmpty
	@CPF
	private String cpf;
	
	@NotEmpty
	@Length(min = 5, max = 200)
	private String razaoSocial;
	
	@NotEmpty
	@CNPJ
	private String cnpj;
	
	public PJDto() {
	}

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

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "PJDto ["
				+ " id=" + this.id
				+ " nome=" + this.nome
				+ " email=" + this.email
				+ " senha=" + this.senha
				+ " cpf=" + this.cpf
				+ " razaoSocial=" + this.razaoSocial
				+ " cnpj=" + this.cnpj
				+ " ]";
	}
	
}
