package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServicoPadrao extends Servico {

	private List<Passo> passos;
	private Orcamento orcamento;
	private float custoAtual;
	private int passoAtualOrcamento;
	//TODO - adicionar tempoPassoAtual aqui e no diagrama de classes, e alterar todos os metodos necessarios (neste momento nao estao a contabilizar os preços por tempo)
	//TODO - passo tem de ter custoPecas isolado

	//Construtores

	private void AuxiliarConstrutor(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento){
		setId(id);
		setIdCliente(idCliente);
		setAbandonado(false);
		passos 				= new ArrayList<>();
		orcamento 			= new Orcamento(passosOrcamento, descricaoOrcamento);
		custoAtual 			= 0;
		passoAtualOrcamento = -1;
	}

	/** Construtor ServicoPadrao para situação normal, i.e., orcamento feito, vai esperar pela resposta do cliente **/
	public ServicoPadrao(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento) {
		AuxiliarConstrutor(id, idCliente, passosOrcamento, descricaoOrcamento);
		setIdTecnico(null); //É alterado depois de ser atribuido a um técnico
		setEstado(EstadoServico.AguardarConfirmacao);
		setDataConclusao(null);
	}

	/** ServicoPadrao iniciado com o Técnico a null. Apenas lhe é atribuido o técnico posteriormente por meio de um setter **/
	public ServicoPadrao(String id, String idTecnico, String idCliente, String descricaoOrcamento) {
		AuxiliarConstrutor(id, idCliente, null, descricaoOrcamento);
		setIdTecnico(idTecnico);
		setEstado(EstadoServico.Irreparavel);
		setDataConclusao(LocalDateTime.now());
	}


	//Clone

	private ServicoPadrao(ServicoPadrao sp) {
		setEstado(sp.getEstado());
		setId(sp.getId());
		setIdTecnico(sp.getIdTecnico());
		setIdCliente(sp.getIdCliente());
		setAbandonado(sp.getAbandonado());
		setDataConclusao(sp.getDataConclusao());
		this.passos              = sp.getPassos();
		this.orcamento           = sp.getOrcamento();
		this.custoAtual          = sp.getCustoAtual();
		this.passoAtualOrcamento = sp.getPassoAtualOrcamento();
	}

	@Override
	public Servico clone() {
		return new ServicoPadrao(this);
	}


	//getters e setters

	//TODO: Luis acho q n é esta Lista de passos q tu queres!! Mas sim a do orçamento
	//return orcamento.listarPassosOrcamento();
	public List<Passo> getPassos() { return passos.stream().map(Passo::clone).collect(Collectors.toList()); }

	public Orcamento getOrcamento() { return orcamento.clone(); }

	private float getCustoAtual() { return custoAtual; }

	private int getPassoAtualOrcamento() { return passoAtualOrcamento; }

	/**
	 * 
	 * @param custo
	 * @param descricao
	 * @param tempo
	 */
	public void addPasso(int custo, String descricao, int tempo) {
		passos.add(new Passo(custo, descricao, tempo));
	}

	public Passo proxPasso() {
		passoAtualOrcamento++;
		Passo novoPasso = orcamento.getPasso(passoAtualOrcamento);
		if(novoPasso != null) {
			passos.add(novoPasso);
			return passos.get(passos.size() - 1).clone();
		}
		return null;
	}

	public Passo getPassoAtual() {
		if(passos.size() > 0)
			return passos.get(passos.size() - 1).clone();
		return null;
	}

	public boolean verificaCusto() {
		// TODO - implement ServicoPadrao.verificaCusto
		throw new UnsupportedOperationException();
	}

	public float getCusto() {
		// TODO - implement ServicoPadrao.getCusto
		throw new UnsupportedOperationException();
	}



	// Mudar Estado

	@Override
	public boolean mudaEstado(EstadoServico estado) {
		EstadoServico estadoAtual = getEstado();
		if(estadoAtual == EstadoServico.AguardarConfirmacao) 	 return aceitarOuRejeitarOrcamento(estado);
		else if(estadoAtual == EstadoServico.EsperandoReparacao) return executarServico(estado);
		else if(estadoAtual == EstadoServico.EmExecucao) 		 return interromperOuConcluirOuIrreparavel(estado);
		else if(estadoAtual == EstadoServico.Interrompido) 		 return retomarServico(estado);
		else return false;
	}

	private boolean aceitarOuRejeitarOrcamento(EstadoServico estado){
		if(estado == EstadoServico.OrcamentoRecusado || estado == EstadoServico.EsperandoReparacao){
			setEstado(estado);
			return true;
		}
		return false;
	}

	private boolean executarServico(EstadoServico estado) {
		if (estado == EstadoServico.EmExecucao){
			setEstado(estado);
			return true;
		}
		return false;
	}

	private boolean interromperOuConcluirOuIrreparavel(EstadoServico estado){
		if(estado == EstadoServico.Irreparavel || estado == EstadoServico.Concluido || estado == EstadoServico.Interrompido){
			setEstado(estado);
			return true;
		}
		return false;
	}

	private boolean retomarServico(EstadoServico estado){
		if(estado == EstadoServico.EmExecucao){
			setEstado(estado);
			return true;
		}
		return false;
	}

	public float duracaoPassos(){ //todo adicionar ao diagrama de classes
		return 0;  //todo
	}

	public float duracaoPassosPrevistos(){//todo adicionar ao diagrama de classes
		return 0; //todo
	}


	// Auxiliares

	@Override
	public int compareTo(Object o) {
		if(o instanceof Servico){
			return getDataConclusao().compareTo(((Servico) o).getDataConclusao());
		}
		return -1;
	}
}