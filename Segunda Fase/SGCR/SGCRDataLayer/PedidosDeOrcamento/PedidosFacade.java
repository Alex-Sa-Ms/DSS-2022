package SGCRDataLayer.PedidosDeOrcamento;

import java.io.Serializable;
import java.util.*;

public class PedidosFacade implements Serializable {

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
		filaPedidos.addLast(new PedidoOrcamento(descricao,idEquip,NIF));
	}

	public PedidoOrcamento getProxPedido() {
		return filaPedidos.poll();
	}

}