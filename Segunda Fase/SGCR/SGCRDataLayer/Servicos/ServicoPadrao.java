package SGCRDataLayer.Servicos;

import java.util.*;

public class ServicoPadrao extends Servico {

	private List<Passo> passos;
	private Orcamento orcamento;
	private float custoAtual;
	private int passoAtualOrcamento;

	public ServicoPadrao(String id, String idTecnico, String idCliente, List<Passo> passos, String descricao) {
		setId(id);
		setIdCliente(idCliente);
		setIdTecnico(idTecnico);
		setEstado(EstadoServico.AguardarConfirmacao);
		setAbandonado(false);
		setDataConclusao(null);
		passos    			= new ArrayList<>();
		orcamento 			= new Orcamento(passos, descricao);
		custoAtual 			= 0;
		passoAtualOrcamento = 0; //TODO - Ver se é neste valor q deve começar
	}

	/**
	 * 
	 * @param custo
	 * @param descricao
	 * @param tempo
	 */
	public void addPasso(int custo, java.lang.String descricao, int tempo) {
		// TODO - implement ServicoPadrao.addPasso
		throw new UnsupportedOperationException();
	}

	public Passo proxPasso() {
		// TODO - implement ServicoPadrao.proxPasso
		throw new UnsupportedOperationException();
	}

	public Passo getPassoAtual() {
		// TODO - implement ServicoPadrao.getPassoAtual
		throw new UnsupportedOperationException();
	}

	public boolean verificaCusto() {
		// TODO - implement ServicoPadrao.verificaCusto
		throw new UnsupportedOperationException();
	}

	public float getCusto() {
		// TODO - implement ServicoPadrao.getCusto
		throw new UnsupportedOperationException();
	}

}