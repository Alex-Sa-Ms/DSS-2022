package SGCRDataLayer.Servicos;

public class Passo {

	private float custoPecas;
	private String descricao;
	private float tempo;

	/** Construtor para os passos de orçamento. Não se espera que sejam modificados **/
	public Passo(float custoPecas, String descricao, float tempo) {
		if(custoPecas < 0 || tempo < 0 || descricao == null) throw new IllegalArgumentException();
		this.custoPecas = custoPecas;
		this.descricao  = descricao;
		this.tempo 	    = tempo;
	}

	/** Construtor para os passos do servico. Valores podem vir a ser alterados **/
	public Passo(float custoPecas, String descricao) {
		if(custoPecas < 0 || descricao == null) throw new IllegalArgumentException();
		this.custoPecas = custoPecas;
		this.descricao  = descricao;
		this.tempo 	    = 0;
	}

	public Passo clone(){ return new Passo(custoPecas, descricao, tempo); }

	public float getCustoPecas() { return custoPecas; }

	public void setCustoPecas(float custoPecas) { this.custoPecas = custoPecas; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public float getTempo() { return tempo; }

	public void setTempo(float tempo) { this.tempo = tempo; }

	public void addTempo(float tempo) { this.tempo += tempo; }
}