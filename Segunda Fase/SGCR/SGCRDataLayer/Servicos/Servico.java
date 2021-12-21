package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;

public abstract class Servico {

	EstadoServico estado;
	private String id;
	private LocalDateTime dataConclusao;
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