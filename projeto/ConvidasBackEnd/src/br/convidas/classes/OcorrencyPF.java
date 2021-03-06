package br.convidas.classes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ocorrency_pf")
public class OcorrencyPF {
	@Id
    @SequenceGenerator(name="SEQ", sequenceName="GEN_OPF_ID", allocationSize=1)
    @GeneratedValue(generator="SEQ",strategy= GenerationType.SEQUENCE)
	private Integer id;
	private Date date;
	@Column(length=9999)
	private String description;
	@ManyToOne
	@JoinColumn(name="pf_id", referencedColumnName="id")
	private PessoaFisica pessoFisica;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PessoaFisica getPessoFisica() {
		return pessoFisica;
	}
	public void setPessoFisica(PessoaFisica pessoFisica) {
		this.pessoFisica = pessoFisica;
	}
}
