package SGCRLogicLayer;

import SGCRDataLayer.Clientes.ClientesFacade;
import SGCRDataLayer.Clientes.FichaCliente;
import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;
import SGCRDataLayer.Funcionarios.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

 public interface iSGCR {

	// ****** Iniciar/Terminar Sessao ******
	/**
	 * Inicia a sessão de um funcionário.
	 * @param ID Identificador do funcionário
	 * @param  Password Senha iniciar a sessão
	 * @return -1 caso o login esteja incorreto
	 * 			0 caso seja um funcionario de balcao
	 * 			1 caso seja um tecnico
	 * 			2 caso seja um gestor
	 */
	 int login(String ID, String Password);

	/**
	 * Fecha a sessão do funcionário.
	 * @return true caso um utilizador esteja logado, false caso contrário
	 */
	 boolean logout();


	// ****** Metodos relativos a Clientes ******

	/**
	 * Cria uma ficha de cliente.
	 * @param nome Nome do cliente.
	 * @param nif NIF do cliente (será o seu identificador).
	 * @param email Email do cliente.
	 * @return falso caso o nif ja esteja registado
	 */
	 boolean criaFichaCliente(String nome, String nif, String email);

	/**
	 * Verifica se o cliente está registado no sistema.
	 * @param idCliente Identificador do cliente.
	 * @return true caso o cliente possua uma ficha de Cliente.
	 */
	 boolean existeCliente(String idCliente);

	/**
	 * Lista todas as fichas de cliente do sistema.
	 * @return lista de fichas de clientes.
	 */
	 List<FichaCliente> listarFichasCLiente();


	// ****** Metodos relativos a Pedidos de Orcamento ******

	/**
	 * Cria um pedido de orcamento.
	 * @param descricao Descricao do problema.
	 * @param nifCliente NIF do cliente.
	 */
	 boolean criarNovoPedido(String descricao, String nifCliente);

	/**
	 * Pedir o proximo pedido do orcamento.
	 * @return Proximo pedido de orcamento
	 */
	 PedidoOrcamento resolverPedido();

	/**
	 * Listar todos os pedidos de orcamento.
	 * @return Lista de pedidos de orcamento.
	 */
	 List<PedidoOrcamento> listarPedidos();

	// ****** Metodos relativos a Funcionarios ******

	/**
	 * Cria uma conta de funcionario de balcao.
	 * @param id Identificador do funcionario
	 * @param password Senha que permite iniciar a sessão
	 * @return false caso já exista um funcionario com o mesmo identificador.
	 */
	 boolean adicionaFuncBalcao(String id, String password);

	/**
	 * Cria uma conta de tecnico.
	 * @param id Identificador do funcionario
	 * @param password Senha que permite iniciar a sessão
	 * @return false caso já exista um funcionario com o mesmo identificador.
	 */
	 boolean adicionaTecnico(String id, String password);

	/**
	 * Lista todos os funcionarios de balcao.
	 * @return lista de funcionarios de balcao.
	 */
	 List<FuncionarioBalcao> listarFuncionariosBalcao();

	/**
	 * Lista todos os tecnicos.
	 * @return lista de tecnicos.
	 */
	 List<Tecnico> listarTecnicos();


	// ****** Metodos relativos a Servicos ******

	/**
	 * Cria servico padrao
	 * @param o  orcamento relativo ao servico
	 * @param passos relativos a resolucao
	 * @return true caso o pedido tenha sido adicionado ao sistema.
	 */
	 boolean criaServicoPadrao(PedidoOrcamento o, List<Passo> passos);

	/**
	 * Cria servico expresso
	 * @param custo de reparacao do produto
	 * @param NIF do cliente
	 * @return true caso o pedido tenha sido adicionado ao sistema.
	 */
	 boolean criarServicoExpresso(Float custo, String NIF,String descricao);

	/**
	 * Definir o preço de uma hora de trabalho.
	 * @param precoHora a definir.
	 * @return true caso seja um preco a hora valido (>0).
	 */
	 boolean definirPrecoHoraServicos(float precoHora);

	/**
	 * Calcular o prazo maximo de uma reparação.
	 * @return data maxima para a reparação.
	 */
	 LocalDateTime calcularPrazoMaximo(List<Passo> passos);

	/**
	 * Rejeitar um pedido de orcamento e cria um serviço irreparavel.
	 * @return true se o pedido foi rejeitado.
	 */
	 boolean rejeitaPedidoOrcamento(PedidoOrcamento o);

	/**
	 * Rejeitar um servico e cria um serviço irreparavel.
	 * @return true se o servico foi rejeitado.
	 */
	 boolean rejeitaServico(String id);


	/**
	 * Aceita um orcamento e muda o estado do serviço para espera de reparacao.
	 * @return true se o  estado do servico foi alterado para espera de reparacao.
	 */
	 boolean aceitarOrcamento(String idServico);

	/**
	 * Rejeita um orcamento muda o estado do servico para orcamento recusado.
	 * @return true se o  estado do servico foi alterado para orcamento recusado.
	 */
	 boolean rejeitarOrcamento(String idServico);

	/**
	 * Comeca um servico e altera o seu estado para em execucao
	 * @return servico caso haja um servico para ser reparado
	 * 		   null caso contrário
	 */
	 Servico comecarServico();

	/**
	 * Muda o estado de um servico em execucao para interrompido
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja em execucao
	 */
	 boolean interromperServico(String IDServico);

	/**
	 * Muda o estado de um servico interrompido para em execucao
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja interrompido
	 */
	 Passo retomarServico(String IDServico);

	/**
	 * Conclui um servico, envia email e atualizado dados do tecnico
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja em execucao
	 */
	 boolean concluiServico(String IDServico);

	/**
	 * Entrega um equipamento
	 * @param idServico relativo a reparacao
	 * @return preço da reparacao
	 */
	 float entregarEquipamento(String idServico);


	/**
	 * Adiciona um passo de reparacao a um servico padrao
	 * @param IDServico relativo a reparacao
	 * @param passo adicionado ao conjunto de passos de reparacao
	 * @return true caso o servico esteja em execucao e seja um servico padrao
	 */
	 boolean addPassoServico(String IDServico, Passo passo);

	/**
	 * Obtem o passo em que o tecnico se encontra na reparacao
	 * @param idServico relativo a reparacao
	 * @return passo atual se o servico existir
	 */
	 Passo getPassoAtual(String idServico);

	/**
	 * Obtem o proximo passo para a execucao da reparacao
	 * @param IDServico relativo a reparacao
	 * @return proximo passo caso exista
	 */
	Passo proxPasso(String IDServico) throws CustoExcedidoException;

	/**
	 * Lista todos os passos necessarios para a reparacao do equipamento
	 * @param IDServico relativo a reparacao
	 * @return lista de passos
	 */
	 List<Passo> listarPassosServico(String IDServico);

	/**
	 * Lista servicos em espera de confirmacao de orcamento
	 * @return lista de servicos
	 */
	 List<Servico> listarServicosEmEsperaDeConfirmacao();

	/**
	 * Lista todos os servicos a espera de reparacao
	 * @return lista de servicos
	 */
	 List<Servico> listarServicosPendentes();

	/**
	 * Lista todos os servicos interrompidos
	 * @return lista de servicos
	 */
	 List<Servico> listarServicosInterrompidos();

	/**
	 * Lista todos os servicos prontos para leventamento de um cliente
	 * @param NIF do cliente cujo servicos é desejado observar
	 * @return lista de servicos
	 */
	 List<Servico> listarServicosProntosLevantamento(String NIF);

	/**
	 * Lista todas a intervencoes de cada tecnico
	 * @return mapa com cujas keys correspondem ao identificador do tecnico
	 * e cujos values correspondem a um treeset de servicos organizados pela data
	 */
	 Map<String, TreeSet<Servico>> listaIntervencoes();

	/**
	 * Estatisticas acerca dos tecnicos
	 * @return lista de tecnico stats (incluem o numero de reparacoes expresso e padrao,
	 * para alem da duracao media de cada repracao e da media do desvio do tempo das reparacoes
	 * em relacao ao tempo esperado)
	 */
	 List<TecnicoStats> estatisticasEficienciaCentro();

	/**
	 * Estatisticas acerca dos funcionarios de balcao
	 * @return lista de balcaoStats (icluem o numero de entregas e rececoes)
	 */
	 List<BalcaoStats> rececoes_e_entregas();


	/**
	 * Carrega dados de um ficheiro
	 * @param s Nome do ficheiro cujo os dados vão ser carregados
	 * @return 0 caso a operação tenha sido bem decorrida
	 * 		   -1 caso contrario
	 */
	 int load(String s);

	/**
	 * Guarda as informacoes referentes a data layer num ficheiro
	 * @param filepath Nome do ficheiro cujo os dados vão ser guardados
	 * @return 0 caso a operação tenha sido bem decorrida
	 * 		   -1 caso o logout falhe
	 * 		   1 caso não consiga encontrar o filepath
	 * 		   2 caso haja uma IOException
	 */
	 int encerraAplicacao(String filepath);


	 class CustoExcedidoException extends Exception{
		 public CustoExcedidoException() {}
	 }
}