import java.util.*;

public class SGCRFacade implements iSGCR {

	FichaCliente clientes;
	Collection<Funcionario> funcionarios;
	Collection<PedidoOrcamento> filaPedidoOrcamentos;
	Servico servico;
	private int permissao;
	private String idUtilizador;

	@Override
	public boolean login(String ID, String Password) {
		return false;
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
	public List<Servico> listarServicosConcluidos(String NIF) {
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
	public PedidoOrcamento resolverPedido() {
		return null;
	}

	@Override
	public String comecarServico() {
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
	public boolean interrromperServico(String IDServico) {
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
	public boolean criaServicoPadrao(Orcamento orcamento, PedidoOrcamento pedido) {
		return false;
	}

	@Override
	public boolean criaFichaCliente(FichaCliente nova) {
		return false;
	}

	@Override
	public boolean criarServicoExpresso(ServicoExpresso novo, String NIF) {
		return false;
	}

	@Override
	public boolean criarNovoPedido(PedidoOrcamento novo) {
		return false;
	}
}