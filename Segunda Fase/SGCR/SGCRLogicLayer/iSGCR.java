package SGCRLogicLayer;

import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;
import SGCRDataLayer.Funcionarios.*;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface iSGCR {

	/**
	 * 
	 * @param ID
	 * @param Password
	 */
	int login(java.lang.String ID, java.lang.String Password);

	boolean logOut();

	int encerraAplicacao(); //todo adicionar retornar int no diagrama

	List<Servico> listarServicosPendentes();

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
	List<Servico> listarServicosProntosLevantamento(java.lang.String NIF);

	/**
	 *
     * @param idServico
     * @return
     */
	float entregarEquipamento(String idServico);

	/**
	 * 
	 * @param custo
	 * @param NIF
	 */
	boolean criarServicoExpresso(Float custo, java.lang.String NIF); //todo mudar parametros do diagrama (a interface não fornece um id para o equipamento)

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
	 * @param
	 * @param passos
	 */
	boolean criaServicoPadrao(PedidoOrcamento p, List<Passo> passos);

	/**
	 * 
	 * @param o
	 */
	boolean rejeitaPedidoOrcamento(PedidoOrcamento o);

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
	 */
	boolean addPassoServico(String ID, Passo passo);

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

	Statistics estatisticas();

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

	List<Servico> listarServicosInterrompidos();

	boolean existeCliente(String idCliente);

	List<PedidoOrcamento>listarPedidos();

}