package SGCRDataLayer.Servicos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Orcamento implements Serializable {

	private final List<Passo> passos; //lista de passos que constituem o orcamento
	private float precoPrevisto = 0;  //preco calculado a partir das estimativas inseridas em cada passo
	private float tempoPrevisto = 0;  //tempo calculado a partir das estimativas inseridas em cada passo
	private final String descricao;   //string que define a razao pela qual foi efetuado o orcamento
	private final LocalDateTime data; //Data em que o orcamento foi criado

	/**
	 * Construtor de um Orcamento
	 * @param passos lista de passos que vão constituir o orcamento
	 * @param descricao string que define a razao pela qual foi efetuado o orcamento
	 */
	public Orcamento(List<Passo> passos, String descricao) {
		if(descricao == null) throw new IllegalArgumentException();
		this.passos    = passos == null ? new ArrayList<>() : passos.stream().map(Passo::clone).collect(Collectors.toList());
		this.descricao = descricao;
		data  		   = LocalDateTime.now();
		calculaValoresPrevisto();
	}

	/** Calcula os valores para o tempo e custo do servico, tendo em conta as estimativas inseridas nos passos que constituem o orcamento. */
	private void calculaValoresPrevisto(){
		for(Passo p : passos){
			precoPrevisto += p.getCusto();
			tempoPrevisto += p.getTempo();
		}
	}

	/** @return lista com os passos (clonados) que constituem o orcamento */
	public List<Passo> listarPassosOrcamento() {
		return passos.stream().map(Passo::clone).collect(Collectors.toList());
	}

	/**
	 * @param indice inteiro que indicada a posicao da qual se pretende obter um passo
	 * @return passo (clonado) situado na posicao indicada pelo indice fornecido,
	 * ou 'null' caso não exista uma posicao com o indice fornecido
	 */
	public Passo getPasso(int indice){
		if(indice < passos.size())
			return passos.get(indice).clone();
		else return null;
	}

	public float getPrecoPrevisto() { return precoPrevisto; }

	/** @return tempo previsto, em minutos, para executar o servico. */
	public float getTempoPrevisto() { return tempoPrevisto; }

	public String getDescricao() { return descricao; }

	public LocalDateTime getData() { return data; }

	public Orcamento clone(){ return new Orcamento(this.passos, this.descricao); }

	//TODO: Remover depois da app estar feita

	@Override
	public String toString() {
		return "Orcamento{" +
				"passos=" + passos +
				", precoPrevisto=" + precoPrevisto +
				", tempoPrevisto=" + tempoPrevisto +
				", descricao='" + descricao + '\'' +
				'}';
	}
}