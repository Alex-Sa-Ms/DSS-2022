package SGCRDataLayer.Servicos;

import java.io.Serializable;

public class Passo implements Serializable {

	private float custoPecas;
	private String descricao;
	private float tempo;
	private static float precoHora = (float) 4.5;

	/** Construtor para os passos de orçamento.
	 * @param custoPecas float que indica o custo das pecas necessárias para executar o passo
	 * @param descricao String que descreve o passo
	 * @param tempo Tempo, em minutos, necessário para executar o passo
	 */
	public Passo(float custoPecas, String descricao, float tempo) {
		if(custoPecas < 0 || tempo < 0 || descricao == null) throw new IllegalArgumentException();
		this.custoPecas = custoPecas;
		this.descricao  = descricao;
		this.tempo 	    = tempo;
	}

	/** Construtor para os passos do servico. Variavel tempo inicializada a 0.
	 * @param custoPecas float que indica o custo das pecas necessárias para executar o passo
	 * @param descricao String que descreve o passo
	 */
	public Passo(float custoPecas, String descricao) {
		if(custoPecas < 0 || descricao == null) throw new IllegalArgumentException();
		this.custoPecas = custoPecas;
		this.descricao  = descricao;
		this.tempo 	    = 0;
	}

	public Passo clone(){ return new Passo(custoPecas, descricao, tempo); }

	public float getCustoPecas() { return custoPecas; }

	public void setCustoPecas(float custoPecas) { this.custoPecas = custoPecas; }

	public float getCusto() { return custoPecas + (tempo / 60) * precoHora; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public float getTempo() { return tempo; }

	public void setTempo(float tempo) { this.tempo = tempo; }

	public void addTempo(float tempo) { this.tempo += tempo; }

	public float getPrecoHora() { return precoHora; }

	static boolean setPrecoHora(float precoHora) {
		if(precoHora <= 0) return false;
		Passo.precoHora = precoHora;
		return true;
	}

	//TODO: Remover depois da app estar feita

	@Override
	public String toString() {
		return "Passo{" +
				"custoPecas=" + custoPecas +
				", descricao='" + descricao + '\'' +
				", tempo=" + tempo +
				", precoHora=" + precoHora +
				'}';
	}
}