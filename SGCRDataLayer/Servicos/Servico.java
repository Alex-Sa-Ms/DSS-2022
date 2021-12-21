package SGCRDataLayer.Servicos;

public abstract class Servico {

	EstadoServico estado;
	private String id;
	private java.time.LocalDateTime dataConclusao;
	private String idTecnico;
	private String idCliente;
	private Boolean Abandonado;

	/**
	 * 
	 * @param estado
	 */
	public boolean mudaEstado(EstadoServico estado) {
		// TODO - implement Servico.mudaEstado
		throw new UnsupportedOperationException();
	}

}