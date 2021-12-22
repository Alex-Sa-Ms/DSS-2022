package SGCRDataLayer.Servicos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Orcamento {

	private final List<Passo> passos;
	private float precoPrevisto = 0;
	private float tempoPrevisto = 0;
	private final String descricao;
	private final float precoHora = (float) 4.5;  //TODO - O resto do programa deve ter em conta este valor, talvez defini-lo uma vez no servicosFacade

	public Orcamento(List<Passo> passos, String descricao) {
		if(descricao == null) throw new IllegalArgumentException();
		this.passos    = passos == null ? new ArrayList<>() : passos.stream().map(Passo::clone).collect(Collectors.toList());
		this.descricao = descricao;
		calculaValoresPrevisto();
	}

	/*
	public Orcamento(List<Passo> passos, String descricao, float precoHora) {
		if(descricao == null && precoHora < 0) throw new IllegalArgumentException();
		this.passos        = passos == null ? new ArrayList<>() : passos.stream().map(Passo::clone).collect(Collectors.toList());
		this.descricao     = descricao;
		calculaValoresPrevisto();
	}*/

	private void calculaValoresPrevisto(){
		for(Passo p : passos){
			precoPrevisto += p.getCustoPecas();
			tempoPrevisto += p.getTempo();
		}
		precoPrevisto += tempoPrevisto * precoHora;
	}

	public List<Passo> listarPassosOrcamento() {
		return passos.stream().map(Passo::clone).collect(Collectors.toList());
	}

	public float getPrecoPrevisto() { return precoPrevisto; }

	public float getTempoPrevisto() { return tempoPrevisto; }

	public String getDescricao() { return descricao; }

	public Passo getPasso(int indice){
		if(indice < passos.size())
			return passos.get(indice).clone();
		else return null;
	}

	public float getPrecoHora() { return precoHora; }

	public Orcamento clone(){ return new Orcamento(this.passos, this.descricao); }
}