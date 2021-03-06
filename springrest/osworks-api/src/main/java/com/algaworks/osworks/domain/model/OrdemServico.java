package com.algaworks.osworks.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.algaworks.osworks.domain.ValidationGroups;
import com.algaworks.osworks.domain.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class OrdemServico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Long id;
	
	/*@Valid //colocando aqui aciona o Bean Validation na model Cliente 
	@ConvertGroup(from = Default.class, to = ValidationGroups.ClienteId.class ) 
	 					Essa anotação foi necessaria para criar um grupo em comum com a model Cliente, se entrar em Cliente veja que lá o atributo Id esta com a seguindo anotação:
	 					para o Id: @NotNull(groups = ValidationGroups.ClienteId.class) sendo assim o OrdemServico valida caso seja passado o Cliente sem ID mas ao cadastro o cliente
	 					não sendo informado o Id será gerado automaticamente pois essa BeanValidation de Notnull para ID só vai rolar com o group definido aqui através da Interface
	 					que foi craida ValidationGroups, essa interface esta criada no packagem domain*/
	
	
	/*IMPORTANTE: Como para OrdemServico estamos usando o Representation Model (veja o package api.model) e lá na classe 
	 * OrdemServicoInput estão as validações não se faz mais necessario ter as Bean Validation aqui no Model que será 
	 * persistido no banco, só faria sentindo se fosse ser cadastrado , persistido no banco de dados sem ser através da API 
	 * e do Representation Model OndemServicoInput
	*/
	//@NotNull
	@ManyToOne
	//@JoinColumn(name = "id_cliente")  // não usando @JoinColum por padrão a columa na tabela do BD fica cliente_id
	private Cliente cliente;
	
	//@NotBlank
	private String descricao;
	
	//@NotNull
	private BigDecimal preco;
	
	//@JsonProperty(access = Access.READ_ONLY) //retirado por conta da explicação no comentario acima "IMPORTANTE"
	@Enumerated(EnumType.STRING)
	private StatusOrdemServico status;
	
	//@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataAbertura;
	
	//@JsonProperty(access = Access.READ_ONLY)  // com essa anotação impeço que venham dados via Json para a propriedade
	private OffsetDateTime dataFinalizacao;
	
	@OneToMany (mappedBy = "ordemServico")
	private List<Comentario> comentarios = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public StatusOrdemServico getStatus() {
		return status;
	}
	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}
	public OffsetDateTime getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(OffsetDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public OffsetDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}
	public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		OrdemServico other = (OrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean podeSerFianlizada() {
		return StatusOrdemServico.ABERTA.equals(getStatus());
	}
	
	public boolean naoPodeSerFinalizada() {
		return !podeSerFianlizada();
	}
	
	public void finalizar() {
		
		if ( naoPodeSerFinalizada()) {
			throw new NegocioException("Ordem de serviço não pode ser finalizada!");
		}
		setStatus(StatusOrdemServico.FINALIZADA);
		setDataFinalizacao(OffsetDateTime.now());
		
	}
}
