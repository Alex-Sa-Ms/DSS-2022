package SGCRLogicLayer;

import SGCRDataLayer.Clientes.*;
import SGCRDataLayer.Funcionarios.*;
import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SGCRFacade implements iSGCR, Serializable {
	private String caminho = "save";
	private ClientesFacade clientesFacade;
	private FuncionarioFacade funcionarioFacade;
	private PedidosFacade pedidosFacade;
	private ServicosFacade servicosFacade;
	private int permissao;
	private String idUtilizador;


	//-1 login incorreto
	// 0 funcionario balcao
	// 1 tecnico
	// 2 gestor
	public SGCRFacade(){
		this.clientesFacade    = new ClientesFacade();
		this.pedidosFacade     = new PedidosFacade();
		this.funcionarioFacade = new FuncionarioFacade();
		this.servicosFacade    = new ServicosFacade();

	}

	// ****** Iniciar/Terminar Sessao ******

	@Override
	public int logIn(String ID, String Password) {
		if((permissao = funcionarioFacade.verificaCredenciais(ID,Password)) != -1){
			idUtilizador = ID;
		}
		return permissao;
	}

	@Override
	public boolean logOut() {
		if (permissao != -1 && idUtilizador != null){
			permissao =- 1;
			idUtilizador = null;
			return true;
		}else return false;
	}


	// ****** Metodos relativos a Clientes ******

	@Override
	public boolean criaFichaCliente(String nome, String nif, String email) {
		if(permissao == 0)
			return clientesFacade.criaFichaCliente(nome, nif, email);
		return false;
	}

	@Override
	public boolean existeCliente(String idCliente) {
		if(permissao == 0){
			return (clientesFacade.getFichaCliente(idCliente) != null);
		}
		return false;
	}


	// ****** Metodos relativos a Pedidos de Orcamento ******

	@Override
	public boolean criarNovoPedido(String descricao, String nifCliente) {
		if(permissao == 0){
			if(clientesFacade.getFichaCliente(nifCliente) != null){
				funcionarioFacade.incNrRececoes(idUtilizador);
				pedidosFacade.addPedido(descricao, clientesFacade.getIdProxEquip(), nifCliente);
				return true;
			}
		}
		return false;
	}

	@Override
	public PedidoOrcamento resolverPedido() {
		if(permissao == 1)
			return pedidosFacade.getProxPedido();
		return null;
	}


	// ****** Metodos relativos a Funcionarios ******

	@Override
	public boolean adicionaFuncBalcao(String id, String password) {
		if(permissao == 2)
			return funcionarioFacade.addFuncBalcao(id, password);
		return false;
	}

	@Override
	public boolean adicionaTecnico(String id, String password) {
		if(permissao == 2)
			return funcionarioFacade.addTecnico(id, password);
		return false;
	}

	@Override
	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		if(permissao == 2)
			return funcionarioFacade.listarFuncionariosBalcao();
		return null;
	}

	@Override
	public List<Tecnico> listarTecnicos() {
		if(permissao == 2)
			return funcionarioFacade.listarTecnicos();
		return null;
	}


	// ****** Metodos relativos a Servicos ******

	@Override
	public boolean criaServicoPadrao(PedidoOrcamento o, List<Passo> passos) {
		if(permissao == 1){
			return servicosFacade.addServicoPadrao(o.getIdEquipamento(), o.getNIFCliente(), passos, o.getDescricao());
		} return false;
	}

	@Override
	public boolean criarServicoExpresso(Float custo, String NIF) {
		if(permissao == 0) {
			funcionarioFacade.incNrRececoes(idUtilizador);
			return servicosFacade.addServicoExpresso(clientesFacade.getIdProxEquip(), NIF, custo);
		}
		return false;
	}

	@Override
	public boolean rejeitaPedidoOrcamento(PedidoOrcamento o) {
		if(permissao == 1){
			if(servicosFacade.addServicoPadraoIrreparavel(o.getIdEquipamento(), o.getNIFCliente(), idUtilizador, o.getDescricao())){
				EmailHandler.emailIrreparavel(); //TODO isto tem de receber argumentos
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean aceitarOrcamento(String idServico) {
		if(permissao >= 0) //TODO - vejam q permissao querem aqui
			return servicosFacade.orcamentoAceite(idServico);
		return false;
	}

	@Override
	public boolean rejeitarOrcamento(String idServico) {
		if(permissao >= 0) //TODO - vejam q permissao querem aqui
			return servicosFacade.orcamentoRejeitado(idServico);
		return false;
	}

	@Override
	public Servico comecarServico() {
		if (permissao == 1){
			Servico servico = servicosFacade.getProxServico(idUtilizador);
			if(servico != null){
				funcionarioFacade.addServicoTecnico(idUtilizador, servico.getId()); //TODO - cagamos no caso de se isto retornar falso?
				return servico;
			}
		}
		return null;
	}

	@Override
	public boolean interromperServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico))
			return servicosFacade.interrompeServico(IDServico);
		return false;
	}

	@Override
	public Passo retomarServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico))
			return servicosFacade.retomaServico(IDServico) ? servicosFacade.getPassoAtual(IDServico) : null;
		return null;
	}

	@Override
	public boolean concluiServico(String IDServico) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)){

			if(servicosFacade.concluiServico(IDServico)){
				Servico s = servicosFacade.getServico(IDServico);
				if(s instanceof ServicoPadrao) {
					ServicoPadrao sp = (ServicoPadrao) s;
					funcionarioFacade.incNrRepProgConcluidas(idUtilizador, sp.duracaoPassos(), sp.duracaoPassosPrevistos());
				}
				else funcionarioFacade.incNrRepExpConcluidas(idUtilizador);  //assumindo que não criamos novos servicos //TODO - luis wdym?
				return true;
			}

		}
		return false;
	}

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

	@Override
	public boolean addPassoServico(String ID, Passo passo) {
		if (permissao==1){
			return servicosFacade.addPasso(ID, passo);
		}
		return false;
	}

	@Override
	public Passo proxPasso(String IDServico) {
		if (permissao==1){
			return servicosFacade.proxPasso(IDServico); //TODO - Se isto crashar, culpo a interface
		}
		return null;
	}

	@Override
	public List<Passo> listarPassosServico(String IDServico) {
		if(permissao==1){
			return ((ServicoPadrao)servicosFacade.getServico(IDServico)).getPassos();
		}
		return null;
	}

	@Override
	public List<Servico> listarServicosPendentes() {
		if(permissao == 1)
			return servicosFacade.listaServicosPendentes();
		return null;
	}

	@Override
	public List<Servico> listarServicosProntosLevantamento(String NIF) {
		if(permissao == 0){
			Set<String> idsEquips = clientesFacade.getFichaCliente(NIF).getEquipamentos();
			List<Servico> servicos = new ArrayList<>();
			for (String id: idsEquips){
				Servico newServico = servicosFacade.getServico(id);
				if(prontoParaLevantar(newServico))
					servicos.add(newServico);
			}
			return servicos;
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

	//list
	@Override
	public Statistics estatisticas() { //todo mudar o return value no diagrama
		if (permissao==2) {
			return new Statistics(servicosFacade, funcionarioFacade);
		}
		return null;
	}

	@Override
	public Map<String, TreeSet<Servico>> listaIntervencoes() { //todo ja fazemos isto na estatisticas (remover do diagrama)
		if(permissao==2) {
			//return servicosFacade.getServicos().stream().collect(Collectors.groupingBy(Servico::getIdTecnico, Collectors.toCollection(TreeSet::new))); //Ordem natural imposta pelo comparable do servico
			return servicosFacade.listaIntervencoes();
		}
		return null;
	}

	@Override
	public List<Servico> listarServicosInterrompidos() {
		if(permissao==1) {
			return funcionarioFacade.listarServicosTecnico(idUtilizador)
					.stream()
					.map(e -> servicosFacade.getServico(e))
					.filter(e -> e.getEstado() == EstadoServico.Interrompido)
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<PedidoOrcamento> listarPedidos() {
		if(permissao==0){
			return new ArrayList<>(pedidosFacade.getFilaPedidos());
		}
		return null;
	}

	// ****** Iniciar/Encerrar Aplicacao ******

	public static iSGCR loadSGCRFacade(){ //Deserialize
		try {
			SGCRFacade novo;
			FileInputStream fileIn = new FileInputStream("save");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			novo = (SGCRFacade) in.readObject();
			in.close();
			fileIn.close();
			return novo;
		} catch (IOException | ClassNotFoundException fnfe){
			return null;
		}
	}

	//TODO - Deve ter em atencao o estado em que o Timer se encontra. Deve matar a thread e ao iniciar o programa voltar a inicia-la. Nao deveria terminar a sessao?
	@Override
	public int encerraAplicacao() { //Serialize
		if(logOut()){
			try {
				FileOutputStream fileOut;
				fileOut = new FileOutputStream(caminho);
				ObjectOutputStream out;
				out = new ObjectOutputStream(fileOut);
				out.writeObject(this);
				out.flush();
				out.close();
				fileOut.close();
			} catch (FileNotFoundException fnfe){
				return 1;
			} catch (IOException e){
				return 2;
			}
		}
		return 0;
	}
}
