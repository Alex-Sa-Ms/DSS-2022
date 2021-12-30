package SGCRDataLayer.PedidosDeOrcamento;

import java.io.Serializable;
import java.util.List;

public interface iPedidos extends Serializable{

    /**
     * @return lista de pedidos de orçamento
     */
    List<PedidoOrcamento> getFilaPedidos();

    /**
     * adiciona um pedido à fila de pedidos
     * @param descricao string que descreve o problema do equipamento
     * @param idEquip identificador do equipamento
     * @param NIF identificador do cliente
     */
    void addPedido(String descricao, String idEquip, String NIF);

    /** @return o primeiro pedido na fila */
    PedidoOrcamento getProxPedido();

}
