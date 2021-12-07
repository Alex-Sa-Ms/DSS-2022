import java.util.List;

public interface iSGCR {

	/**
	 * 
	 * @param ID
	 * @param Password
	 */
	boolean login(java.lang.String ID, java.lang.String Password);

	List<PedidoOrcamento> listarPedidos();

	List<Servico> listarServicosPendentes();

	/**
	 * 
	 * @param novo
	 */
	boolean criarNovoPedido(PedidoOrcamento novo);

	/**
	 * 
	 * @param NIF
	 */
	List<Servico> listarServicosConcluidos(java.lang.String NIF);

	/**
	 * 
	 * @param idServico
	 */
	boolean pagamentoServico(java.lang.String idServico);

	/**
	 * 
	 * @param idServico
	 */
	boolean entregarEquipamento(java.lang.String idServico);

	/**
	 * 
	 * @param novo
	 * @param NIF
	 */
	boolean criarServicoExpresso(ServicoExpresso novo, java.lang.String NIF);

	/**
	 * 
	 * @param nova
	 */
	boolean criaFichaCliente(FichaCliente nova);

	PedidoOrcamento resolverPedido();

	/**
	 * 
	 * @param orcamento
	 * @param pedido
	 */
	boolean criaServicoPadrao(Orcamento orcamento, PedidoOrcamento pedido);

	String comecarServico();

	/**
	 * 
	 * @param IDServico
	 */
	Passo proxPasso(String IDServico);

	/**
	 * 
	 * @param IDServico
	 */
	List<Passo> listarPassosServico(java.lang.String IDServico);

	/**
	 * 
	 * @param ID
	 * @param passo
	 * @param index
	 */
	boolean addPassoServico(String ID, Passo passo, int index);

	/**
	 * 
	 * @param IDServico
	 */
	boolean interrromperServico(String IDServico);

	/**
	 * 
	 * @param IDServico
	 */
	Passo retomarServico(String IDServico);

	void estatisticas();

}