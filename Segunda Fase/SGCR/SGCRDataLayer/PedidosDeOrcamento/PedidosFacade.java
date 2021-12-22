package SGCRDataLayer.PedidosDeOrcamento;

import java.util.*;

public class PedidosFacade {

	Deque<PedidoOrcamento> filaPedidos;

	public Deque<PedidoOrcamento> getFilaPedidos() {
		return filaPedidos;
	}

	public void setFilaPedidos(Deque<PedidoOrcamento> filaPedidos) {
		this.filaPedidos = filaPedidos;
	}

	/**
	 * 
	 * @param descricao
	 * @param idEquip
	 * @param NIF
	 */
	public void addPedido(String descricao, String idEquip, String NIF) {
		// TODO - implement PedidosFacade.addPedido
		throw new UnsupportedOperationException();
	}

	public PedidoOrcamento getProxPedido() {
		// TODO - implement PedidosFacade.getProxPedido
		throw new UnsupportedOperationException();
	}

}