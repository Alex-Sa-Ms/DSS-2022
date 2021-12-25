package SGCRLogicLayer;

import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;
import SGCRDataLayer.Funcionarios.*;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface iSGCR {

	// ****** Iniciar/Terminar Sessao ******

	/**
	 * 
	 * @param ID
	 * @param Password
	 */
	int logIn(String ID, String Password);

	boolean logOut();


	// ****** Metodos relativos a Clientes ******

	/**
	 *
	 * @param nome
	 * @param nif
	 * @param email
	 */
	boolean criaFichaCliente(String nome, String nif, String email);

	/**
	 *
	 * @param idCliente
	 * @return
	 */
	boolean existeCliente(String idCliente);


	// ****** Metodos relativos a Pedidos de Orcamento ******

	/**
	 *
	 * @param descricao
	 * @param nifCliente
	 */
	boolean criarNovoPedido(String descricao, String nifCliente);

	PedidoOrcamento resolverPedido();


	// ****** Metodos relativos a Funcionarios ******

	/**
	 *
	 * @param id
	 * @param password
	 */
	boolean adicionaFuncBalcao(String id, String password);

	/**
	 *
	 * @param id
	 * @param password
	 */
	boolean adicionaTecnico(String id, String password);

	List<FuncionarioBalcao> listarFuncionariosBalcao();

	List<Tecnico> listarTecnicos();


	// ****** Metodos relativos a Servicos ******

	/**
	 *
	 * @param
	 * @param passos
	 */
	boolean criaServicoPadrao(PedidoOrcamento p, List<Passo> passos);

	/**
	 *
	 * @param custo
	 * @param NIF
	 */
	boolean criarServicoExpresso(Float custo, String NIF);

	/**
	 *
	 * @param o
	 */
	boolean rejeitaPedidoOrcamento(PedidoOrcamento o);

	boolean aceitarOrcamento(String idServico);

	boolean rejeitarOrcamento(String idServico);

	Servico comecarServico();

	/**
	 *
	 * @param IDServico
	 */
	boolean interromperServico(String IDServico);

	/**
	 *
	 * @param IDServico
	 */
	Passo retomarServico(String IDServico);

	/**
	 *
	 * @param IDServico
	 */
	boolean concluiServico(String IDServico);

	/**
	 *
	 * @param idServico
	 * @return
	 */
	float entregarEquipamento(String idServico);

	/**
	 *
	 * @param ID
	 * @param passo
	 */
	boolean addPassoServico(String ID, Passo passo);

	/**
	 *
	 * @param IDServico
	 */
	Passo proxPasso(String IDServico);

	/**
	 *
	 * @param IDServico
	 */
	List<Passo> listarPassosServico(String IDServico);

	List<Servico> listarServicosPendentes();

	/**
	 * 
	 * @param NIF
	 */
	List<Servico> listarServicosProntosLevantamento(String NIF);


	Statistics estatisticas();

	Map<String, TreeSet<Servico>> listaIntervencoes();

	List<Servico> listarServicosInterrompidos();

	List<PedidoOrcamento>listarPedidos();


	// ****** Iniciar/Encerrar Aplicacao ******

	static iSGCR loadSGCRFacade() { return null; }

	/**
	 * Encerra a aplicacao, guardando o estado desta.
	 * @return 0 se não houve problemas, 1 se ocorreu um erro do tipo 'FileNotFound' e 2 se ocorreu um erro a escrever o estado da aplicacao para um ficheiro
	 */
	int encerraAplicacao();
}