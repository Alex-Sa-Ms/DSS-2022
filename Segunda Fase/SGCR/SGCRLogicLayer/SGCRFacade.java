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
		this.clientesFacade = new ClientesFacade();
		this.pedidosFacade = new PedidosFacade();
		this.funcionarioFacade = new FuncionarioFacade();
		this.servicosFacade = new ServicosFacade();

	}


	public static SGCRFacade loadSGCRFacade(){ //Dessirialize
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

	@Override
	public int login(String ID, String Password) {
		if((permissao=funcionarioFacade.verificaCredenciais(ID,Password))!=-1){
			idUtilizador=ID;
		}
		return permissao;
	}

	@Override
	public boolean logOut() {
		if (permissao!=-1 && idUtilizador!=null){
			permissao=-1;
			idUtilizador=null;
			return true;
		}else return false;
	}

	//0 não há problemas
	//1 fileNotFound
	//2 erro ao escrever ficheiro
	@Override
	public int encerraAplicacao() { //Serialize
		if(logOut()){
			try {
				FileOutputStream fileOut;
				fileOut = new FileOutputStream(caminho);
				ObjectOutputStream out;
				out = new ObjectOutputStream(fileOut);
				out.writeObject(this);
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

	@Override
	public List<Servico> listarServicosPendentes() {
		if(permissao==1){
			return servicosFacade.listaPedidosPendentes();
		} else return null;
	}


	@Override
	public boolean criarNovoPedido(String descricao, String nifCliente) {
		if(permissao==0){
			if(clientesFacade.getFichaCliente(nifCliente)!=null){
				funcionarioFacade.incNrRececoes(idUtilizador);
				pedidosFacade.addPedido(descricao, clientesFacade.getIdProxEquip() ,nifCliente);
				return true;
			}
		}
		return false;
	}

	private boolean prontoParaLevantar(Servico s){
		return (s.getEstado() == EstadoServico.OrcamentoRecusado ||
				s.getEstado() ==	EstadoServico.Irreparavel ||
				s.getEstado() ==	EstadoServico.Concluido ||
				s.getEstado() ==	EstadoServico.Expirado);
	}

	@Override
	public List<Servico> listarServicosProntosLevantamento(String NIF) {
		if(permissao==0){
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
	public boolean criaFichaCliente(String nome, String nif, String email) {
		if(permissao==0){
			return clientesFacade.criaFichaCliente(nome,nif,email);
		}
		return false;
	}

	@Override
	public PedidoOrcamento resolverPedido() {
		if(permissao==1){
			return pedidosFacade.getProxPedido();
		} else return null;
	}

	@Override
	public boolean criaServicoPadrao(PedidoOrcamento o, List<Passo> passos) {
		if(permissao==1){
			return servicosFacade.addServicoPadrao(o.getIdEquipamento(), o.getNIFCliente(), passos, o.getDescricao());
		} return false;
	}

	@Override
	public boolean rejeitaPedidoOrcamento(PedidoOrcamento o) {
		if(permissao==1){
			if(servicosFacade.addServicoPadraoIrreparavel(o.getIdEquipamento(), o.getNIFCliente(), idUtilizador, o.getDescricao())){
				EmailHandler.emailIrreparavel();
				return true;
			}
		}
		return false;
	}

	@Override
	public Servico comecarServico() {
		if (permissao==1){
			Servico servico = servicosFacade.getProxServico(idUtilizador);
			if(servico!=null){
				funcionarioFacade.addServicoTecnico(idUtilizador,servico.getId()); //TODO - cagamos no caso de se isto retornar falso?
				return servico;
			}
		}
		return null;
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
	public boolean addPassoServico(String ID, Passo passo) {
		if (permissao==1){
			return servicosFacade.addPasso(ID, passo);
		}
		return false;
	}

	@Override
	public boolean interromperServico(String IDServico) {
		if (permissao==1){
			return servicosFacade.interrompeServico(IDServico);
		}
		return false;
	}

	@Override
	public boolean concluiServico(String IDServico) {
		if (permissao==1){
			Servico s;
			if((s= servicosFacade.getServico(IDServico))!=null){
				servicosFacade.concluiServico(IDServico);            //nao lidamos com o falso aqui
				if(s instanceof ServicoPadrao)
					funcionarioFacade.incNrRepProgConcluidas(idUtilizador
							,((ServicoPadrao) s).duracaoPassos(),((ServicoPadrao) s).duracaoPassosPrevistos());
				else funcionarioFacade.incNrRepExpConcluidas(idUtilizador);  //assumindo que não criamos novos servicos
				return true;
			}
		}
		return false;
	}

	@Override
	public Passo retomarServico(String IDServico) {
		if (permissao==1){
			Servico s = servicosFacade.getServico(IDServico);
			if(s instanceof ServicoPadrao && funcionarioFacade.listarServicosTecnico(idUtilizador).contains(IDServico)){
				servicosFacade.retomaServico(IDServico);          //nao fazemos verificacao caso de falso
				return ((ServicoPadrao) s).getPassoAtual();
			}
		}
		return null;
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
	public boolean adicionaTecnico(String id, String password) {
		if(permissao==2){
			return funcionarioFacade.addTecnico(id,password);
		}
		return false;
	}

	@Override
	public boolean adicionaFuncBalcao(String id, String password) {
		if(permissao==2){
			return funcionarioFacade.addFuncBalcao(id,password);
		}
		return false;
	}

	@Override
	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		if(permissao==2){
			return funcionarioFacade.listarFuncionariosBalcao();
		}
		return null;
	}

	@Override
	public List<Tecnico> listarTecnicos() {
		if(permissao==2){
			return funcionarioFacade.listarTecnicos();
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
	public boolean existeCliente(String idCliente) {
		if(permissao==0){
			return (clientesFacade.getFichaCliente(idCliente)!=null);
		}
		return false;
	}

	@Override
	public List<PedidoOrcamento> listarPedidos() {
		if(permissao==0){
			return new ArrayList<>(pedidosFacade.getFilaPedidos());
		}
		return null;
	}

	@Override
	public boolean criarServicoExpresso(Float custo, String NIF) {
		if(permissao==0) {
			funcionarioFacade.incNrRececoes(idUtilizador);
			return servicosFacade.addServicoExpresso(clientesFacade.getIdProxEquip(), NIF, custo);
		}
		return false;
	}
}