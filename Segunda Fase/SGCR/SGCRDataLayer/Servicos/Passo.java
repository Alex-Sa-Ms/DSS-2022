package SGCRDataLayer.Servicos;

public class Passo {

	private float custoPecas;
	private String descricao;
	private float tempo;
	private static float precoHora = (float) 4.5;

	/** Construtor para os passos de orçamento.
	 * @param custoPecas
	 * @param descricao
	 * @param tempo
	 */
	public Passo(float custoPecas, String descricao, float tempo) {
		if(custoPecas < 0 || tempo < 0 || descricao == null) throw new IllegalArgumentException();
		this.custoPecas = custoPecas;
		this.descricao  = descricao;
		this.tempo 	    = tempo;
	}

	/** Construtor para os passos do servico. Variavel tempo inicializada a 0.
	 * @param custoPecas
	 * @param descricao
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

	public float getCusto() { return custoPecas + tempo * precoHora; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public float getTempo() { return tempo; }

	public void setTempo(float tempo) { this.tempo = tempo; }

	public void addTempo(float tempo) { this.tempo += tempo; }

	public float getPrecoHora() { return precoHora; }

	static void setPrecoHora(float precoHora) { Passo.precoHora = precoHora; }

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