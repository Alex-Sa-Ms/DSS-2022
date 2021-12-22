package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;

public abstract class Servico {

	private EstadoServico estado;
	private String id;
	private LocalDateTime dataConclusao;
	private String idTecnico;
	private String idCliente;
	private Boolean abandonado;


	//Muda Estado do Servico

	/**
	 * 
	 * @param estado
	 */
	public boolean mudaEstado(EstadoServico estado) {
		// TODO - implement Servico.mudaEstado
		throw new UnsupportedOperationException();
	}

	private boolean aceitarOuRejeitarOrcamento(){
		//TODO
		return false;
	}

	//Getters

	public EstadoServico getEstado() { return estado; }

	public String getId() { return id; }

	public LocalDateTime getDataConclusao() { return dataConclusao; }

	public String getIdTecnico() { return idTecnico; }

	public String getIdCliente() { return idCliente; }

	public Boolean getAbandonado() { return abandonado; }


	//Setters

	public void setId(String id) { this.id = id; }

	public void setIdTecnico(String idTecnico) { this.idTecnico = idTecnico; }

	public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

	public void setEstado(EstadoServico estado) { this.estado = estado; }

	public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

	public void setAbandonado(Boolean abandonado) { this.abandonado = abandonado; }
}