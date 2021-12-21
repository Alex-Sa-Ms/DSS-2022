package SGCRDataLayer.Servicos;

public class Passo {

	private float custo;
	private String descricao;
	private int tempo;

	public Passo(float custo, String descricao, int tempo) {
		if(custo < 0 || tempo < 0 || descricao == null) throw new IllegalArgumentException();
		this.custo 	   = custo;
		this.descricao = descricao;
		this.tempo 	   = tempo;
	}

	public Passo clone(){ return new Passo(custo, descricao, tempo); }

	public float getCusto() { return custo; }

	public void setCusto(float custo) { this.custo = custo; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public int getTempo() { return tempo; }

	public void setTempo(int tempo) { this.tempo = tempo; }
}