package SGCRDataLayer.PedidosDeOrcamento;

import java.io.Serializable;

public class PedidoOrcamento implements Serializable {

	private String idEquipamento;
	private String descricao;
	private String NIFCliente;

	/**
	 * Construtor de um Orcamento
	 * @param idEquipamento lista de passos que vão constituir o orcamento
	 * @param descricao string que define a razao pela qual foi efetuado o pedido
	 * @param NIFCliente string do identificador do cliente
	 */
	public PedidoOrcamento(String idEquipamento, String descricao, String NIFCliente) {
		this.idEquipamento = idEquipamento;
		this.descricao = descricao;
		this.NIFCliente = NIFCliente;
	}
 	/**
	 * @return string que é o identificador do cliente
	 */
	public String getIdEquipamento() {
		return idEquipamento;
	}

	/**
	 * modifica o identificador do equipamento no pedido
	 * @param idEquipamento string do novo identificador do equipamento
	 */
	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	/**
	 * @return string que é a descrição do pedido
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * modifica a descrição do pedido
	 * @param descricao string que é a nova descrição
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return string do identificador do cliente associado ao pedido
	 */
	public String getNIFCliente() {
		return NIFCliente;
	}

	/**
	 * modifica o NIF do cliente associado ao pedido
	 * @param NIFCliente string que é o novo NIF do cliente associado ao pedido
	 */
	public void setNIFCliente(String NIFCliente) {
		this.NIFCliente = NIFCliente;
	}
}