package SGCRDataLayer.PedidosDeOrcamento;

import java.io.Serializable;
import java.util.*;

public class PedidosFacade implements Serializable {

	Deque<PedidoOrcamento> filaPedidos = new ArrayDeque<>();

	public List<PedidoOrcamento> getFilaPedidos() { return new ArrayList<>(filaPedidos); }

	/**
	 * 
	 * @param descricao
	 * @param idEquip
	 * @param NIF
	 */
	public void addPedido(String descricao, String idEquip, String NIF) {
		filaPedidos.addLast(new PedidoOrcamento(descricao, idEquip, NIF));
	}

	public PedidoOrcamento getProxPedido() {
		return filaPedidos.poll();
	}

}