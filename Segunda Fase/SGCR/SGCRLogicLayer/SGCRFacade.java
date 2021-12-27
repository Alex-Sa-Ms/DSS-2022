package SGCRLogicLayer;

import SGCRDataLayer.Clientes.*;
import SGCRDataLayer.Funcionarios.*;
import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SGCRFacade implements iSGCR, Serializable {
	private ClientesFacade clientesFacade;
	private FuncionarioFacade funcionarioFacade;
	private PedidosFacade pedidosFacade;
	private ServicosFacade servicosFacade;
	private int permissao;
	private String idUtilizador;
	private Timer timer;

	public SGCRFacade(){
		this.clientesFacade    = new ClientesFacade();
		this.pedidosFacade     = new PedidosFacade();
		this.funcionarioFacade = new FuncionarioFacade();
		this.servicosFacade    = new ServicosFacade();
		runTimer();

	}

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
	@Override
	public int login(String ID, String Password) {
		if((permissao = funcionarioFacade.verificaCredenciais(ID,Password)) != -1){
			idUtilizador = ID;
		}
		return permissao;
	}

	/**
	 * Fecha a sessão do funcionário.
	 * @return true caso um utilizador esteja logado, false caso contrário
	 */
	@Override
	public boolean logout() {
		if (permissao != -1 && idUtilizador != null){
			permissao =- 1;
			idUtilizador = null;
			return true;
		}else return false;
	}


	// ****** Metodos relativos a Clientes ******

	/**
	 * Cria uma ficha de cliente.
	 * @param nome Nome do cliente.
	 * @param nif NIF do cliente (será o seu identificador).
	 * @param email Email do cliente.
	 * @return falso caso o nif ja esteja registado
	 */

	@Override
	public boolean criaFichaCliente(String nome, String nif, String email) {
		if(permissao == 0)
			return clientesFacade.criaFichaCliente(nome, nif, email);
		return false;
	}

	/**
	 * Verifica se o cliente está registado no sistema.
	 * @param idCliente Identificador do cliente.
	 * @return true caso o cliente possua uma ficha de Cliente.
	 */
	@Override
	public boolean existeCliente(String idCliente) {
		if(permissao == 0){
			return (clientesFacade.getFichaCliente(idCliente) != null);
		}
		return false;
	}

	/**
	 * Lista todas as fichas de cliente do sistema.
	 * @return lista de fichas de clientes.
	 */
	@Override
	public List<FichaCliente> listarFichasCLiente(){
		if(permissao == 0){
			return (clientesFacade.getFichasCLiente());
		}
		return null;
	}


	// ****** Metodos relativos a Pedidos de Orcamento ******

	/**
	 * Cria um pedido de orcamento.
	 * @param descricao Descricao do problema.
	 * @param nifCliente NIF do cliente.
	 */
	@Override
	public boolean criarNovoPedido(String descricao, String nifCliente) {
		if(permissao == 0){
			if(clientesFacade.getFichaCliente(nifCliente) != null){
				funcionarioFacade.incNrRececoes(idUtilizador);
				String idProx = clientesFacade.getIdProxEquip();
				pedidosFacade.addPedido(descricao, idProx, nifCliente);
				clientesFacade.addEquipCliente(idProx,nifCliente);
				return true;
			}
		}
		return false;
	}

	/**
	 * Pedir o proximo pedido do orcamento.
	 * @return Proximo pedido de orcamento
	 */
	@Override
	public PedidoOrcamento resolverPedido() {
		if(permissao == 1)
			return pedidosFacade.getProxPedido();
		return null;
	}

	/**
	 * Listar todos os pedidos de orcamento.
	 * @return Lista de pedidos de orcamento.
	 */
	@Override
	public List<PedidoOrcamento> listarPedidos() {
		if(permissao == 1)
			return pedidosFacade.getFilaPedidos();
		return null;
	}

	// ****** Metodos relativos a Funcionarios ******

	/**
	 * Cria uma conta de funcionario de balcao.
	 * @param id Identificador do funcionario
	 * @param password Senha que permite iniciar a sessão
	 * @return false caso já exista um funcionario com o mesmo identificador.
	 */
	@Override
	public boolean adicionaFuncBalcao(String id, String password) {
		if(permissao == 2)
			return funcionarioFacade.addFuncBalcao(id, password);
		return false;
	}

	/**
	 * Cria uma conta de tecnico.
	 * @param id Identificador do funcionario
	 * @param password Senha que permite iniciar a sessão
	 * @return false caso já exista um funcionario com o mesmo identificador.
	 */
	@Override
	public boolean adicionaTecnico(String id, String password) {
		if(permissao == 2)
			return funcionarioFacade.addTecnico(id, password);
		return false;
	}

	/**
	 * Lista todos os funcionarios de balcao.
	 * @return lista de funcionarios de balcao.
	 */
	@Override
	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		if(permissao == 2)
			return funcionarioFacade.listarFuncionariosBalcao();
		return null;
	}

	/**
	 * Lista todos os tecnicos.
	 * @return lista de tecnicos.
	 */
	@Override
	public List<Tecnico> listarTecnicos() {
		if(permissao == 2)
			return funcionarioFacade.listarTecnicos();
		return null;
	}


	// ****** Metodos relativos a Servicos ******

	/**
	 * Cria servico padrao
	 * @param o  orcamento relativo ao servico
	 * @param passos relativos a resolucao
	 * @return true caso o pedido tenha sido adicionado ao sistema.
	 */
	@Override
	public boolean criaServicoPadrao(PedidoOrcamento o, List<Passo> passos) {
		if(permissao == 1){
			LocalDateTime prazoMaximo = calcularPrazoMaximo(passos);
			boolean ret = servicosFacade.addServicoPadrao(o.getIdEquipamento(), o.getNIFCliente(), passos, o.getDescricao(), prazoMaximo);
			if(ret) EmailHandler.emailOrcamento(clientesFacade.getFichaCliente(o.getNIFCliente()).getEmail(), ((ServicoPadrao) servicosFacade.getServicoNaoArquivado(o.getNIFCliente())).getOrcamento().toString());
			return ret;
		} return false;
	}

	/**
	 * Cria servico expresso
	 * @param custo de reparacao do produto
	 * @param NIF do cliente
	 * @return true caso o pedido tenha sido adicionado ao sistema.
	 */
	@Override
	public boolean criarServicoExpresso(Float custo, String NIF,String descricao) {
		if(permissao == 0) {
			funcionarioFacade.incNrRececoes(idUtilizador);
			String idProx= clientesFacade.getIdProxEquip();
			if(servicosFacade.addServicoExpresso(idProx, NIF, custo,descricao)){
				clientesFacade.addEquipCliente(idProx,NIF);
				return true;
			}

		}
		return false;
	}

	/**
	 * Definir o preço de uma hora de trabalho.
	 * @param precoHora a definir.
	 * @return true caso seja um preco a hora valido (>0).
	 */
	@Override
	public boolean definirPrecoHoraServicos(float precoHora){
		if(permissao == 2)
			return servicosFacade.setPrecoHora(precoHora);
		return false;
	}

	/**
	 * Calcular o prazo maximo de uma reparação.
	 * @return data maxima para a reparação.
	 */
	@Override
	public LocalDateTime calcularPrazoMaximo(List<Passo> passos) {
		float duracaoServicoPrevista = passos != null ? (float) passos.stream().mapToDouble(Passo::getTempo).sum() : 0;
		return servicosFacade.calculaPrazoMaximo(funcionarioFacade.getNrTecnicos(), duracaoServicoPrevista);
	}

	/**
	 * Rejeitar um pedido de orcamento e cria um serviço irreparavel.
	 * @return true se o pedido foi rejeitado.
	 */
	@Override
	public boolean rejeitaPedidoOrcamento(PedidoOrcamento o) {
		if(permissao == 1){
			if(servicosFacade.addServicoPadraoIrreparavel(o.getIdEquipamento(), o.getNIFCliente(), idUtilizador, o.getDescricao())){
				EmailHandler.emailIrreparavel(clientesFacade.getFichaCliente(o.getNIFCliente()).getEmail());
				return true;
			}
		}
		return false;
	}

	/**
	 * Aceita um orcamento e muda o estado do serviço para espera de reparacao.
	 * @return true se o  estado do servico foi alterado para espera de reparacao.
	 */
	@Override
	public boolean aceitarOrcamento(String idServico) {
		if(permissao == 0)
			return servicosFacade.orcamentoAceite(idServico);
		return false;
	}

	/**
	 * Rejeita um orcamento muda o estado do servico para orcamento recusado.
	 * @return true se o  estado do servico foi alterado para orcamento recusado.
	 */

	@Override
	public boolean rejeitarOrcamento(String idServico) {
		if(permissao == 0) {
			EmailHandler.emailCancelado(clientesFacade.getFichaCliente(servicosFacade.getServicoNaoArquivado(idServico).getIdCliente()).getEmail());
			return servicosFacade.orcamentoRejeitado(idServico);
		}
		return false;
	}

	/**
	 * Comeca um servico e altera o seu estado para em execucao
	 * @return servico caso haja um servico para ser reparado
	 * 		   null caso contrário
	 */
	@Override
	public Servico comecarServico() {
		if (permissao == 1){
			Servico servico = servicosFacade.getProxServico(idUtilizador);
			if(servico != null){
				funcionarioFacade.addServicoTecnico(idUtilizador, servico.getId());
				return servico;
			}
		}
		return null;
	}

	/**
	 * Muda o estado de um servico em execucao para interrompido
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja em execucao
	 */
	@Override
	public boolean interromperServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico))
			return servicosFacade.interrompeServico(IDServico);
		return false;
	}

	/**
	 * Muda o estado de um servico interrompido para em execucao
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja interrompido
	 */
	@Override
	public Passo retomarServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico))
			return servicosFacade.retomaServico(IDServico) ? servicosFacade.getPassoAtual(IDServico) : null;
		return null;
	}

	/**
	 * Conclui um servico, envia email e atualizado dados do tecnico
	 * @param IDServico relativo a reparacao
	 * @return true caso o estado inicial do servico seja em execucao
	 */
	@Override
	public boolean concluiServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)){

			if(servicosFacade.concluiServico(IDServico)){
				Servico s = servicosFacade.getServicoNaoArquivado(IDServico);
				EmailHandler.emailPronto(clientesFacade.getFichaCliente(s.getIdCliente()).getEmail());
				if(s instanceof ServicoPadrao) {
					ServicoPadrao sp    = (ServicoPadrao) s;
					float duracaoPassos = sp.duracaoPassos();
					funcionarioFacade.incNrRepProgConcluidas(idUtilizador, duracaoPassos, Math.abs(duracaoPassos - sp.duracaoPassosPrevistos()));
				}
				else funcionarioFacade.incNrRepExpConcluidas(idUtilizador);
				return true;
			}

		}
		return false;
	}

	/**
	 * Entrega um equipamento
	 * @param idServico relativo a reparacao
	 * @return preço da reparacao
	 */
	@Override
	public float entregarEquipamento(String idServico) {
		if(permissao == 0){
			float ret = servicosFacade.entregaEquipamento(idServico);
			if(ret != -1) {
				funcionarioFacade.incNrEntregas(idUtilizador);
				return ret;
			}
		}
		return -1;
	}


	/**
	 * Adiciona um passo de reparacao a um servico padrao
	 * @param IDServico relativo a reparacao
	 * @param passo adicionado ao conjunto de passos de reparacao
	 * @return true caso o servico esteja em execucao e seja um servico padrao
	 */

	@Override
	public boolean addPassoServico(String IDServico, Passo passo) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)){
			return servicosFacade.addPasso(IDServico, passo);
		}
		return false;
	}

	/**
	 * Obtem o passo em que o tecnico se encontra na reparacao
	 * @param idServico relativo a reparacao
	 * @return passo atual se o servico existir
	 */

	@Override
	public Passo getPassoAtual(String idServico){
		if (permissao == 1){
			return servicosFacade.getPassoAtual(idServico);
		}
		return null;
	}

	/**
	 * Obtem o proximo passo para a execucao da reparacao
	 * @param IDServico relativo a reparacao
	 * @return proximo passo caso exista
	 */
	@Override
	public Passo proxPasso(String IDServico) throws CustoExcedidoException {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)) {
			try { return servicosFacade.proxPasso(IDServico); }
			catch (ServicoPadrao.CustoExcedidoException e) {
				EmailHandler.emailExcesso(clientesFacade.getFichaCliente(servicosFacade.getServicoNaoArquivado(IDServico).getIdCliente()).getEmail());
				throw new CustoExcedidoException();
			}
		}
		return null;
	}

	/**
	 * Lista todos os passos necessarios para a reparacao do equipamento
	 * @param IDServico relativo a reparacao
	 * @return lista de passos
	 */
	@Override
	public List<Passo> listarPassosServico(String IDServico) {
		if(permissao == 1) {
			Servico servico = servicosFacade.getServicoNaoArquivado(IDServico);
			if(servico instanceof ServicoPadrao)
				return ((ServicoPadrao) servico).getPassos();
		}
		return null;
	}

	/**
	 * Lista servicos em espera de confirmacao de orcamento
	 * @return lista de servicos
	 */
	@Override
	public List<Servico> listarServicosEmEsperaDeConfirmacao() {
		if(permissao == 0)
			return servicosFacade.listaServicosEmEsperaDeConfirmacao();
		return null;
	}

	/**
	 * Lista todos os servicos a espera de reparacao
	 * @return lista de servicos
	 */
	@Override
	public List<Servico> listarServicosPendentes() {
		if(permissao == 1)
			return servicosFacade.listaServicosPendentes();
		return null;
	}

	/**
	 * Lista todos os servicos interrompidos
	 * @return lista de servicos
	 */
	@Override
	public List<Servico> listarServicosInterrompidos() {
		if(permissao == 1)
			return funcionarioFacade.listarServicosTecnico(idUtilizador)
									.stream()
									.map(e -> servicosFacade.getServicoNaoArquivado(e))
									.filter(e -> e.getEstado() == EstadoServico.Interrompido)
									.collect(Collectors.toList());
		return null;
	}

	/**
	 * Lista todos os servicos prontos para leventamento de um cliente
	 * @param NIF do cliente cujo servicos é desejado observar
	 * @return lista de servicos
	 */
	@Override
	public List<Servico> listarServicosProntosLevantamento(String NIF) {
		if(permissao == 0){
			FichaCliente fichaCliente = clientesFacade.getFichaCliente(NIF);
			if(fichaCliente == null) return new ArrayList<>();
			return fichaCliente.getEquipamentos()
							   .stream()
							   .map(id -> servicosFacade.getServicoNaoArquivado(id))
							   .filter(servico -> servico != null && prontoParaLevantar(servico))
							   .collect(Collectors.toList());
		} else return null;
	}

	/**
	 * @param s Servico, do qual se pretende verificar que se encontra num estado em que o equipamento pode ser levantado.
	 * @return 'true' se o equipamento pode ser levantado, 'false' caso contrario
	 */
	private boolean prontoParaLevantar(Servico s){
		return (s.getEstado() == EstadoServico.OrcamentoRecusado ||
				s.getEstado() == EstadoServico.Irreparavel ||
				s.getEstado() == EstadoServico.Concluido ||
				s.getEstado() == EstadoServico.Expirado);
	}

	/**
	 * Lista todas a intervencoes de cada tecnico
	 * @return mapa com cujas keys correspondem ao identificador do tecnico
	 * e cujos values correspondem a um treeset de servicos organizados pela data
	 */
	@Override
	public Map<String, TreeSet<Servico>> listaIntervencoes() {
		if(permissao == 2) {
			return servicosFacade.listaIntervencoes();
		}
		return null;
	}

	/**
	 * Estatisticas acerca dos tecnicos
	 * @return lista de tecnico stats (incluem o numero de reparacoes expresso e padrao,
	 * para alem da duracao media de cada repracao e da media do desvio do tempo das reparacoes
	 * em relacao ao tempo esperado)
	 */
	@Override
	public List<TecnicoStats> estatisticasEficienciaCentro() {
		if(permissao == 2) {
			return funcionarioFacade.listarTecnicos()
					.stream()
					.map(tecnico -> new TecnicoStats(tecnico.getId(),
							tecnico.getnRepProgramadasConcluidas(),
							tecnico.getnRepExpressoConcluidas(),
							tecnico.getDuracaoMediaRepProg(),
							tecnico.getMediaDesvioRepProg()))
					.collect(Collectors.toList());
		}
		return null;
	}

	/**
	 * Estatisticas acerca dos funcionarios de balcao
	 * @return lista de balcaoStats (icluem o numero de entregas e rececoes)
	 */
	@Override
	public List<BalcaoStats> rececoes_e_entregas() {
		if(permissao == 2) {
			return funcionarioFacade.listarFuncionariosBalcao()
					.stream()
					.map(funcBalcao -> new BalcaoStats(funcBalcao.getId(), funcBalcao.getnEntregas(), funcBalcao.getnRececoes()))
					.collect(Collectors.toList());
		}
		return null;
	}

	// ****** Iniciar/Encerrar Aplicacao ******

	/**
	 * Cria o timer e inicialaza-o
	 */
	private void runTimer() {
		this.timer = new Timer(servicosFacade);
		this.timer.start();
	}

	/**
	 * Espera que o timer esteja a dormir para o interromper, e assim o fechar
	 */
	private void encerraTimer(){
		try {
			timer.getLock().lock();
			timer.interrupt();
		} finally {
			timer.getLock().unlock();
		}
	}

	/**
	 * Carrega dados de um ficheiro
	 * @param s Nome do ficheiro cujo os dados vão ser carregados
	 * @return 0 caso a operação tenha sido bem decorrida
	 * 		   -1 caso contrario
	 */
	@Override
	public int load(String s) {
		try {
			FileInputStream fileIn = new FileInputStream(s);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			this.clientesFacade = (ClientesFacade) in.readObject();
			this.servicosFacade = (ServicosFacade) in.readObject();
			this.funcionarioFacade = (FuncionarioFacade) in.readObject();
			this.pedidosFacade = (PedidosFacade) in.readObject();
			in.close();
			fileIn.close();

			encerraTimer();
			this.runTimer();

			return 0;
		} catch (IOException | ClassNotFoundException | NullPointerException fnfe){
			return -1;
		}
	}

	/**
	 * Guarda as informacoes referentes a data layer num ficheiro
	 * @param filepath Nome do ficheiro cujo os dados vão ser guardados
	 * @return 0 caso a operação tenha sido bem decorrida
	 * 		   -1 caso o logout falhe
	 * 		   1 caso não consiga encontrar o filepath
	 * 		   2 caso haja uma IOException
	 */
	@Override
	public int encerraAplicacao(String filepath) { //Serialize
		encerraTimer();
		if (logout()) {
			try {
				FileOutputStream fileOut;
				fileOut = new FileOutputStream(filepath);
				ObjectOutputStream out;
				out = new ObjectOutputStream(fileOut);
				out.writeObject(this.clientesFacade);
				out.writeObject(this.servicosFacade);
				out.writeObject(this.funcionarioFacade);
				out.writeObject(this.pedidosFacade);
				out.flush();
				out.close();
				fileOut.close();
			} catch (FileNotFoundException fnfe) {
				return 1;
			} catch (IOException e) {
				return 2;
			}
			return 0;
		} else return -1;
	}
}
