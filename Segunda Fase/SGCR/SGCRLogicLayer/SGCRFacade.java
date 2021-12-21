package SGCRLogicLayer;

import SGCRDataLayer.Clientes.*;
import SGCRDataLayer.Funcionarios.*;
import SGCRDataLayer.PedidosDeOrcamento.*;
import SGCRDataLayer.Servicos.*;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class SGCRFacade implements iSGCR {
	private ClientesFacade clientesFacade;
	private FuncionarioFacade funcionarioFacade;
	private PedidosFacade pedidosFacade;
	private ServicosFacade servicosFacade;
	private int permissao;
	private String idUtilizador;

	@Override
	public boolean login(String ID, String Password) {
		if(funcionarioFacade.verificaCredenciais(ID,Password)!=-1){
			idUtilizador=ID;
			return true;
		}
		return false;
	}

	@Override
	public boolean logOut() {
		return false;
	}

	@Override
	public void encerraAplicacao() {

	}

	@Override
	public List<PedidoOrcamento> listarPedidos() {
		return null;
	}

	@Override
	public List<Servico> listarServicosPendentes() {
		return null;
	}


	@Override
	public boolean criarNovoPedido(String descricao, String nifCliente) {
		return false;
	}

	@Override
	public List<Servico> listarServicosProntosLevantamento(String NIF) {
		return null;
	}

	@Override
	public boolean pagamentoServico(String idServico) {
		return false;
	}

	@Override
	public boolean entregarEquipamento(String idServico) {
		return false;
	}

	@Override
	public boolean criaFichaCliente(String nome, String nif, String email) {
		return false;
	}

	@Override
	public PedidoOrcamento resolverPedido() {
		return null;
	}

	@Override
	public boolean criaServicoPadrao(PedidoOrcamento pedido, List<Passo> passos) {
		return false;
	}

	@Override
	public boolean rejeitaPedidoOrcamento(PedidoOrcamento pedido) {
		return false;
	}

	@Override
	public Servico comecarServico() {
		return null;
	}

	@Override
	public Passo proxPasso(String IDServico) {
		return null;
	}

	@Override
	public List<Passo> listarPassosServico(String IDServico) {
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