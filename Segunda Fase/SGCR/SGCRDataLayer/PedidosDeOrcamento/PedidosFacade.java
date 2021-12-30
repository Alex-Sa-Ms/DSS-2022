package SGCRDataLayer.PedidosDeOrcamento;

import java.util.*;

public class PedidosFacade implements iPedidos {

	Deque<PedidoOrcamento> filaPedidos = new ArrayDeque<>();

	/**
	 * @return lista de pedidos de orçamento
	 */
	public List<PedidoOrcamento> getFilaPedidos() { return new ArrayList<>(filaPedidos); }

	/**
	 * adiciona um pedido à fila de pedidos
	 * @param descricao string que descreve o problema do equipamento
	 * @param idEquip identificador do equipamento
	 * @param NIF identificador do cliente
	 */
	public void addPedido(String descricao, String idEquip, String NIF) {
		filaPedidos.addLast(new PedidoOrcamento(idEquip, descricao, NIF));
	}
	/** @return o primeiro pedido na fila */
	public PedidoOrcamento getProxPedido() {
		return filaPedidos.poll();
	}

}