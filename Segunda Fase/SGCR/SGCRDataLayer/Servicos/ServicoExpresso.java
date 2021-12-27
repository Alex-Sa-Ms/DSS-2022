package SGCRDataLayer.Servicos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServicoExpresso extends Servico implements Serializable {

	private float custo;
	private String descricao;

	//Construtor

	/** Construtor ServicoPadrao para situação normal, i.e., orcamento feito, vai esperar pela resposta do cliente **/
	public ServicoExpresso(String id, String idCliente, float custo, String descricao) {
		setId(id);
		setIdCliente(idCliente);
		setAbandonado(false);
		setIdTecnico(null); //É alterado depois de ser atribuido a um técnico
		setEstado(EstadoServico.EsperandoReparacao);
		setDataConclusao(null);
		this.custo = custo;
		this.descricao = descricao;
	}

	// Clone

	private ServicoExpresso(ServicoExpresso sp) {
		setEstado(sp.getEstado());
		setId(sp.getId());
		setIdTecnico(sp.getIdTecnico());
		setIdCliente(sp.getIdCliente());
		setAbandonado(sp.getAbandonado());
		setDataConclusao(sp.getDataConclusao());
		this.custo = sp.getCusto();
		this.descricao = sp.descricao;
	}

	@Override
	public Servico clone() {
		return new ServicoExpresso(this);
	}


	//Setters e Getters

	/** @return float que indica o custo do serviço */
	public float getCusto() {
		return this.custo;
	}

	/** @return string com a descricao do servico */
	public String getDescricao() { return this.descricao; }

	@Override
	public boolean mudaEstado(EstadoServico estado) {
		if(getEstado() == EstadoServico.EsperandoReparacao && estado == EstadoServico.EmExecucao) {
			setEstado(EstadoServico.EmExecucao);
			return true;
		}
		else if(getEstado() == EstadoServico.EmExecucao && estado == EstadoServico.Concluido){
			setEstado(EstadoServico.Concluido);
			setDataConclusao(LocalDateTime.now());
			return true;
		}
		return false;
	}


	//TODO: Remover depois da app estar feita
	@Override
	public String toString() {
		return "ServicoExpresso{" + super.toString() +
				", custo=" + custo +
				'}';
	}
}