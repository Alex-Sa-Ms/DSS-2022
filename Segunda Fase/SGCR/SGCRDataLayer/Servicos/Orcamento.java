package SGCRDataLayer.Servicos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Orcamento {

	private final List<Passo> passos;
	private final float precoPrevisto;
	private final int tempoPrevisto;
	private final String descricao;

	public Orcamento(List<Passo> passos, String descricao) {
		if(descricao == null) throw new IllegalArgumentException();
		this.passos        = passos == null ? new ArrayList<>() : passos.stream().map(Passo::clone).collect(Collectors.toList());
		this.precoPrevisto = calculaPrecoPrevisto();
		this.tempoPrevisto = calculaTempoPrevisto();
		this.descricao     = descricao;
	}

	private int calculaTempoPrevisto() {
		//TODO
		return 0;
	}

	private float calculaPrecoPrevisto() {
		//TODO
		return 0;
	}

	public List<Passo> listarPassosOrcamento() {
		return passos.stream().map(Passo::clone).collect(Collectors.toList());
	}

	public float getPrecoPrevisto() { return precoPrevisto; }

	public int getTempoPrevisto() { return tempoPrevisto; }

	public String getDescricao() { return descricao; }

	public Orcamento clone(){
		return new Orcamento(this.passos, this.descricao);
	}
}