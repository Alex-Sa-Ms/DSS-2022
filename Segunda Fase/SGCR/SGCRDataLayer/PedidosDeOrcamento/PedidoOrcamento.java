package SGCRDataLayer.PedidosDeOrcamento;

public class PedidoOrcamento {

	private String idEquipamento;
	private String descricao;
	private String NIFCliente;

	public PedidoOrcamento(String idEquipamento, String descricao, String NIFCliente) {
		this.idEquipamento = idEquipamento;
		this.descricao = descricao;
		this.NIFCliente = NIFCliente;
	}

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNIFCliente() {
		return NIFCliente;
	}

	public void setNIFCliente(String NIFCliente) {
		this.NIFCliente = NIFCliente;
	}
}