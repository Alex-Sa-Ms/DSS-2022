package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;

public class ServicoExpresso extends Servico {

	private float custo;

	//Construtor

	/** Construtor ServicoPadrao para situação normal, i.e., orcamento feito, vai esperar pela resposta do cliente **/
	public ServicoExpresso(String id, String idCliente, float custo) {
		setId(id);
		setIdCliente(idCliente);
		setAbandonado(false);
		setIdTecnico(null); //É alterado depois de ser atribuido a um técnico
		setEstado(EstadoServico.EsperandoReparacao);
		setDataConclusao(null);
		this.custo = custo;
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
	}

	@Override
	public Servico clone() {
		return new ServicoExpresso(this);
	}


	//Setters e Getters

	public float getCusto() {
		return this.custo;
	}

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