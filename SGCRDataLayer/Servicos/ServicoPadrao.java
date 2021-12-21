package SGCRDataLayer.Servicos;

import java.util.*;

public class ServicoPadrao extends Servico {

	Collection<Passo> passos;
	Orcamento orcamento;
	private float custoAtual;
	private int passoAtualOrcamento;

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