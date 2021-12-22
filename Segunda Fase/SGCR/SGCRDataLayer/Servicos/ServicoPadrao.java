package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.util.*;

public class ServicoPadrao extends Servico {

	private List<Passo> passos;
	private Orcamento orcamento;
	private float custoAtual;
	private int passoAtualOrcamento;

	public ServicoPadrao(String id, String idTecnico, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento) {
		setId(id);
		setIdCliente(idCliente);
		setIdTecnico(idTecnico);
		setEstado(EstadoServico.AguardarConfirmacao);
		setAbandonado(false);
		setDataConclusao(null);
		passos    			= new ArrayList<>();
		orcamento 			= new Orcamento(passos, descricaoOrcamento);
		custoAtual 			= 0;
		passoAtualOrcamento = 0; //TODO - Ver se é neste valor q deve começar
	}

	/** ServicoPadrao iniciado com o Técnico a null. Apenas lhe é atribuido o técnico posteriormente por meio de um setter **/
	public ServicoPadrao(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento) {
		setId(id);
		setIdCliente(idCliente);
		setIdTecnico(null);
		setEstado(EstadoServico.AguardarConfirmacao);
		setAbandonado(false);
		setDataConclusao(null);
		passos    			= new ArrayList<>();
		orcamento 			= new Orcamento(passos, descricaoOrcamento);
		custoAtual 			= 0;
		passoAtualOrcamento = 0; //TODO - Ver se é neste valor q deve começar
	}

	/** Construtor ServicoPadrao para um servico Irreparavel **/
	public ServicoPadrao(String id, String idTecnico, String idCliente, String descricao) {
		setId(id);
		setIdCliente(idCliente);
		setIdTecnico(idTecnico);
		setEstado(EstadoServico.Irreparavel);
		setAbandonado(false);
		setDataConclusao(LocalDateTime.now());
		passos    			= new ArrayList<>();
		orcamento 			= new Orcamento(passos, descricao);
		custoAtual 			= 0;
		passoAtualOrcamento = 0; //TODO - Ver se é neste valor q deve começar
	}

	public List<Passo> getPassos() {
		List<Passo> novaLista = new ArrayList<>();
		for (Passo p: this.passos){
			novaLista.add(p.clone());
		}
		return passos;
	}
	/**
	 * 
	 * @param custo
	 * @param descricao
	 * @param tempo
	 */
	public void addPasso(int custo, java.lang.String descricao, int tempo) {
		// TODO - implement ServicoPadrao.addPasso
		throw new UnsupportedOperationException();
	}

	public Passo proxPasso() {
		// TODO - implement ServicoPadrao.proxPasso
		throw new UnsupportedOperationException();
	}

	public Passo getPassoAtual() {
		// TODO - implement ServicoPadrao.getPassoAtual
		throw new UnsupportedOperationException();
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
		if(estadoAtual == EstadoServico.AguardarConfirmacao) return aceitarOuRejeitarOrcamento(estado);
		else if(estadoAtual == EstadoServico.EsperandoReparacao) return executarServico(estado);
		else if(estadoAtual == EstadoServico.EmExecucao) return interromperOuConcluirOuIrreparavel(estado);
		else if(estadoAtual == EstadoServico.Interrompido) return retomarServico(estado);
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

	@Override
	public int compareTo(Object o) {
		if(o instanceof Servico){
			return super.getDataConclusao().compareTo(((Servico) o).getDataConclusao());
		}
		return -1;
	}
}