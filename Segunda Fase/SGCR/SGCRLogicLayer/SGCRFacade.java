package SGCRLogicLayer;

import SGCRDataLayer.Clientes.*;
import SGCRDataLayer.Funcionarios.*;
import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;

import java.util.*;

public class SGCRFacade implements iSGCR {
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

	@Override
	public void encerraAplicacao() { //Serialize
		logOut();
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
		return (s.getEstadoServico() == EstadoServico.OrcamentoRecusado ||
				s.getEstadoServico() ==	EstadoServico.Irreparavel ||
				s.getEstadoServico() ==	EstadoServico.Concluido ||
				s.getEstadoServico() ==	EstadoServico.Expirado);
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
	public boolean pagamentoServico(String idServico) { //Todo
		return false;
	}

	@Override
	public boolean entregarEquipamento(String idServico) {
		if(permissao==0){
			funcionarioFacade.incNrEntregas(idUtilizador);
			return servicosFacade.entregaEquipamento(idServico);
		}
		return false;
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
	public boolean criaServicoPadrao(PedidoOrcamento pedido, List<Passo> passos) {
		if(permissao==1){
			return servicosFacade.addServicoPadrao(pedido.getIdEquipamento(),pedido.getDescricao(),passos);
		} return false;
	}

	@Override
	public boolean rejeitaPedidoOrcamento(PedidoOrcamento pedido) {
		if(permissao==1){
			if(addServicoIrreparavel(pedido.getIdEquipamento(),pedido.getDescricao())){
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
				funcionarioFacade.addServicoTecnico(idUtilizador,servico.getID()); //TODO cagamos no caso de se isto retornar falso?
				return servico;
			}
		}
		return null;
	}

	@Override
	public Passo proxPasso(String IDServico) {
		if (permissao==1){
			return ((ServicoPadrao)servicosFacade.getServico(IDServico)).proxPasso(); //Se isto crashar, culpo a interface
		}
		return null;
	}

	@Override
	public List<Passo> listarPassosServico(String IDServico) {
		if(permissao==1){
			return ((ServicoPadrao)servicosFacade.getServico(IDServico).getPassos());
		}
		return null;
	}

	@Override
	public boolean addPassoServico(String ID, Passo passo, int index) {
		return false;
	}

	@Override
	public boolean interromperServico(String IDServico) {
		return false;
	}

	@Override
	public boolean concluiServico(String IDServico) {
		return false;
	}

	@Override
	public Passo retomarServico(String IDServico) {
		return null;
	}

	@Override
	public void estatisticas() {

	}

	@Override
	public boolean entregaEquipamento(String idEquip) {
		return false;
	}

	@Override
	public boolean adicionaTecnico(String id, String password) {
		return false;
	}

	@Override
	public boolean adicionaFuncBalcao(String id, String password) {
		return false;
	}

	@Override
	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		return null;
	}

	@Override
	public List<Tecnico> listarTecnicos() {
		return null;
	}

	@Override
	public Map<String, TreeSet<Servico>> listaIntervencoes() {
		return null;
	}

	@Override
	public boolean criarServicoExpresso(ServicoExpresso novo, String NIF) {
		return false;
	}
}