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

	public Passo clone(){
		//TODO
		return null;
	}

}