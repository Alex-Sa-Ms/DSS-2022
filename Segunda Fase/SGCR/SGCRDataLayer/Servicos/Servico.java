package SGCRDataLayer.Servicos;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Servico implements Comparable<Servico>, Serializable {

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
	public abstract boolean mudaEstado(EstadoServico estado);

	//Getters

	public EstadoServico getEstado() { return estado; }

	public String getId() { return id; }

	public LocalDateTime getDataConclusao() { return dataConclusao; }

	public String getIdTecnico() { return idTecnico; }

	public String getIdCliente() { return idCliente; }

	public Boolean getAbandonado() { return abandonado; }

	public abstract String getDescricao();

	public abstract float getCusto();

	//Setters

	protected void setId(String id) { this.id = id; }

	protected void setIdTecnico(String idTecnico) { this.idTecnico = idTecnico; }

	protected void setIdCliente(String idCliente) { this.idCliente = idCliente; }

	protected void setEstado(EstadoServico estado) { this.estado = estado; }

	protected void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

	protected void setAbandonado(Boolean abandonado) { this.abandonado = abandonado; }

	public abstract Servico clone();

	// ****** Auxiliares ******

	@Override
	public int compareTo(Servico s) {
		return getDataConclusao().compareTo(s.getDataConclusao());
	}

	//TODO: Remover depois da app estar feita
	@Override
	public String toString() {
		return  "estado=" + estado +
				", id='" + id + '\'' +
				", dataConclusao=" + dataConclusao +
				", idTecnico='" + idTecnico + '\'' +
				", idCliente='" + idCliente + '\'' +
				", abandonado=" + abandonado +
				"";
	}
}