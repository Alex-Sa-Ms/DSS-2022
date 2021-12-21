import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;
import SGCRDataLayer.Funcionarios.*;

public interface iSGCR {

	/**
	 * 
	 * @param ID
	 * @param Password
	 */
	boolean login(java.lang.String ID, java.lang.String Password);

	boolean logOut();

	void encerraAplicacao();

	List<PedidoOrcamento> listarPedidos();

	List<Servico> listarServiçosPendentes();

	/**
	 * 
	 * @param descricao
	 * @param nifCliente
	 */
	boolean criarNovoPedido(String descricao, String nifCliente);

	/**
	 * 
	 * @param NIF
	 */
	List<Servicos> listarServicosProntosLevantamento(java.lang.String NIF);

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
	boolean criarServiçoExpresso(ServicoExpresso novo, java.lang.String NIF);

	/**
	 * 
	 * @param nome
	 * @param nif
	 * @param email
	 */
	boolean criaFichaCliente(String nome, String nif, String email);

	PedidoOrcamento resolverPedido();

	/**
	 * 
	 * @param pedido
	 * @param passos
	 */
	boolean criaServicoPadrao(PedidoOrcamento pedido, List<Passo> passos);

	/**
	 * 
	 * @param pedido
	 */
	boolean rejeitaPedidoOrcamento(PedidoOrcamento pedido);

	Servico comecarServico();

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
	boolean interromperServico(String IDServico);

	/**
	 * 
	 * @param IDServico
	 */
	boolean concluiServico(String IDServico);

	/**
	 * 
	 * @param IDServico
	 */
	Passo retomarServico(String IDServico);

	void estatisticas();

	/**
	 * 
	 * @param idEquip
	 */
	boolean entregaEquipamento(String idEquip);

	/**
	 * 
	 * @param id
	 * @param password
	 */
	boolean adicionaTecnico(String id, String password);

	/**
	 * 
	 * @param id
	 * @param password
	 */
	boolean adicionaFuncBalcao(String id, String password);

	List<FuncionarioBalcao> listarFuncionariosBalcao();

	List<Tecnico> listarTecnicos();

	Map<String, TreeSet<Servico>> listaIntervencoes();

}