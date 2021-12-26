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


	//-1 login incorreto
	// 0 funcionario balcao
	// 1 tecnico
	// 2 gestor
	public SGCRFacade(){
		this.clientesFacade    = new ClientesFacade();
		this.pedidosFacade     = new PedidosFacade();
		this.funcionarioFacade = new FuncionarioFacade();
		this.servicosFacade    = new ServicosFacade();
		runTimer();

	}

	// ****** Iniciar/Terminar Sessao ******

	@Override
	public int login(String ID, String Password) {
		if((permissao = funcionarioFacade.verificaCredenciais(ID,Password)) != -1){
			idUtilizador = ID;
		}
		return permissao;
	}

	@Override
	public boolean logout() {
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

	@Override
	public List<FichaCliente> listarFichasCLiente(){
		if(permissao == 0){
			return (clientesFacade.getFichasCLiente());
		}
		return null;
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

	@Override
	public List<PedidoOrcamento> listarPedidos() {
		if(permissao == 1)
			return pedidosFacade.getFilaPedidos();
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
			boolean ret = servicosFacade.addServicoPadrao(o.getIdEquipamento(), o.getNIFCliente(), passos, o.getDescricao());
			if(ret) {
				LocalDateTime prazoMaximo = calcularPrazoMaximo(passos);
				EmailHandler.emailOrcamento(clientesFacade.getFichaCliente(o.getNIFCliente()).getEmail(),o.toString());
			}
			return ret;
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
	public boolean definirPrecoHoraServicos(float precoHora){
		if(permissao == 2)
			return servicosFacade.setPrecoHora(precoHora);
		return false;
	}

	@Override
	public LocalDateTime calcularPrazoMaximo(List<Passo> passos) {
		float duracaoServicoPrevista = passos != null ? (float) passos.stream().mapToDouble(Passo::getTempo).sum() : 0;
		return servicosFacade.calculaPrazoMaximo(funcionarioFacade.getNrTecnicos(), duracaoServicoPrevista);
	}

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

	@Override
	public boolean aceitarOrcamento(String idServico) {
		if(permissao == 0)
			return servicosFacade.orcamentoAceite(idServico);
		return false;
	}

	@Override
	public boolean rejeitarOrcamento(String idServico) {
		if(permissao == 0) {
			EmailHandler.emailCancelado(clientesFacade.getFichaCliente(servicosFacade.getServico(idServico).getIdCliente()).getEmail());
			return servicosFacade.orcamentoRejeitado(idServico);
		}
		return false;
	}

	@Override
	public Servico comecarServico() {
		if (permissao == 1){
			Servico servico = servicosFacade.getProxServico(idUtilizador);
			if(servico != null && funcionarioFacade.addServicoTecnico(idUtilizador, servico.getId())){
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
	public boolean addPassoServico(String IDServico, Passo passo) {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)){
			return servicosFacade.addPasso(IDServico, passo);
		}
		return false;
	}

	@Override
	public Passo getPassoAtual(String idServico){ return servicosFacade.getPassoAtual(idServico); }

	@Override
	public Passo proxPasso(String IDServico) throws CustoExcedidoException {
		if (permissao == 1 && funcionarioFacade.possuiServico(idUtilizador, IDServico)) {
			try { return servicosFacade.proxPasso(IDServico); }
			catch (ServicoPadrao.CustoExcedidoException e) {
				EmailHandler.emailExcesso(clientesFacade.getFichaCliente(servicosFacade.getServico(IDServico).getIdCliente()).getEmail());
				throw new CustoExcedidoException();
			}
		}
		return null;
	}

	@Override
	public List<Passo> listarPassosServico(String IDServico) {
		if(permissao == 1) {
			Servico servico = servicosFacade.getServico(IDServico);
			if(servico instanceof ServicoPadrao)
				return ((ServicoPadrao) servico).getPassos();
		}
		return null;
	}

	@Override
	public List<Servico> listarServicosEmEsperaDeConfirmacao() {
		if(permissao == 0)
			return servicosFacade.listaServicosEmEsperaDeConfirmacao();
		return null;
	}

	@Override
	public List<Servico> listarServicosPendentes() {
		if(permissao == 1)
			return servicosFacade.listaServicosPendentes();
		return null;
	}

	@Override
	public List<Servico> listarServicosInterrompidos() {
		if(permissao == 1)
			return funcionarioFacade.listarServicosTecnico(idUtilizador)
									.stream()
									.map(e -> servicosFacade.getServico(e))
									.filter(e -> e.getEstado() == EstadoServico.Interrompido)
									.collect(Collectors.toList());
		return null;
	}

	@Override
	public List<Servico> listarServicosProntosLevantamento(String NIF) {
		if(permissao == 0){
			FichaCliente fichaCliente = clientesFacade.getFichaCliente(NIF);
			if(fichaCliente == null) return new ArrayList<>();
			return fichaCliente.getEquipamentos()
							   .stream()
							   .map(id -> servicosFacade.getServico(id))
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

	@Override
	public Map<String, TreeSet<Servico>> listaIntervencoes() {
		if(permissao == 2) {
			return servicosFacade.listaIntervencoes();
		}
		return null;
	}

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

	@Override
	public void runTimer() {
		this.timer = new Timer(servicosFacade);
		this.timer.start();
	}

	// ****** Iniciar/Encerrar Aplicacao ******

	@Override
	public int encerraAplicacao(String filepath) { //Serialize
		if(logout()){
			while (timer.isRunning()) {
				try {
					timer.getCond().await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			timer.interrupt();
			try {
				FileOutputStream fileOut;
				fileOut = new FileOutputStream(filepath);
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
			return 0;
		}else return -1;

	}
}
