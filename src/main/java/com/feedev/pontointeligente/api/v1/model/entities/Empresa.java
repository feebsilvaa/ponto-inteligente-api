package com.feedev.pontointeligente.api.v1.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "tb_empresa")
public class Empresa implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6460264670653764372L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "razao_social", nullable = false)
	private String razaoSocial;
	
	@Column(name = "cnpj", nullable = false)
	private String cnpj;

	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;

	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;
	
	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Funcionario> funcionarios;
	
	public Empresa() {
	}
		
	public Empresa(Long id, String razaoSocial, String cnpj, Date dataCriacao, Date dataAtualizacao,
			List<Funcionario> funcionarios) {
		
		if (id != 0L || id != null) {
			this.id = id;
		}
		
		if (!razaoSocial.isEmpty() || razaoSocial != null) {
			this.razaoSocial = razaoSocial;
		}
		
		if (!cnpj.isEmpty() || cnpj != null) {
			this.cnpj = cnpj;
		}
		
		if (dataCriacao != null) {
			this.dataCriacao = dataCriacao;
		}
		
		if (dataAtualizacao != null) {
			this.dataAtualizacao = dataAtualizacao;
		}
		
		if (!funcionarios.isEmpty() || funcionarios != null) {
			this.funcionarios = funcionarios;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	/**
	 * Atualiza automaticamente o campo data_atualizao, a cada update realizado no banco de dados 
	 */
	@PreUpdate
	private void preUpdate() {
		this.dataAtualizacao = new Date();
	}
	
	/**
	 * Salva os campos data_criacao e data_atualizacao com a data atual a cada insert realizado
	 */
	@PrePersist
	private void prePersist() {
		final Date atual = new Date();
		this.dataCriacao = atual;
		this.dataAtualizacao = atual;
	}
	
	@Override
	public String toString() {
		return "Empresa [id=" + this.id + ", razaoSocial=" + this.razaoSocial + ", cnpj=" + this.cnpj + ", dataCriacao=" + this.dataCriacao + ", dataAtualizacao=" + this.dataAtualizacao + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empresa other = (Empresa) obj;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
			
}
