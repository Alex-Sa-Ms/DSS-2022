package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import SGCRLogicLayer.Horas;

public class ServicoPadrao extends Servico {

	private List<Passo> passos;
	private Orcamento orcamento;
	private float custoAtual;
	private int passoAtualOrcamento;
	private long inicioPassoAtual;

	// ****** Construtores ******

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

	// ****** Clone ******

	private ServicoPadrao(ServicoPadrao sp) {
		setEstado(sp.getEstado());
		setId(sp.getId());
		setIdTecnico(sp.getIdTecnico());
		setIdCliente(sp.getIdCliente());
		setAbandonado(sp.getAbandonado());
		setDataConclusao(sp.getDataConclusao());
		this.passos              = sp.getPassos();
		this.orcamento           = sp.getOrcamento();
		this.custoAtual          = sp.getCusto();
		this.passoAtualOrcamento = sp.getPassoAtualOrcamento();
	}

	@Override
	public Servico clone() {
		return new ServicoPadrao(this);
	}


	// ****** getters e setters ******

	//TODO: Luis acho q n é esta Lista de passos q tu queres!! Mas sim a do orçamento
	//return orcamento.listarPassosOrcamento();
	public List<Passo> getPassos() { return passos.stream().map(Passo::clone).collect(Collectors.toList()); }

	public Orcamento getOrcamento() { return orcamento.clone(); }

	/**
	 * @return float que indica o custo do serviço até ao momento
	 */
	private float getCusto() { return custoAtual; }

	private int getPassoAtualOrcamento() { return passoAtualOrcamento; }

	private Passo getUltimoPassoLista(){
		if(passos.size() > 0) return passos.get(passos.size() - 1);
		return null;
	}

	/**
	 * 
	 * @param custo
	 * @param descricao
	 * @param tempo
	 */
	public void addPasso(int custo, String descricao, int tempo) {
		passos.add(new Passo(custo, descricao, tempo));
	}

	/**
	 * @return Próximo 'Passo' a ser executado, ou 'null' caso não exista.
	 */
	public Passo proxPasso() {
		//Guarda o tempo utilizado para executar o passo atual, e atualiza a variavel custoAtual, antes de saltar para o próximo passo
		Passo passo = getUltimoPassoLista();
		if(passo != null) {
			passo.addTempo(Horas.converteTimeMillisParaHoras(System.currentTimeMillis() - inicioPassoAtual));
			custoAtual += passo.getCustoPecas() + passo.getTempo() * orcamento.getPrecoHora();
		}

		passoAtualOrcamento++;
		inicioPassoAtual = System.currentTimeMillis();
		Passo novoPasso  = orcamento.getPasso(passoAtualOrcamento);

		if(novoPasso != null) {
			passos.add(novoPasso);
			return passos.get(passos.size() - 1).clone();
		}
		return null;
	}

	/**
	 * Usado para retomar um servico
	 * @return
	 */
	public Passo getPassoAtual() {
		if(passos.size() > 0)
			return passos.get(passos.size() - 1).clone();
		return null;
	}

	/**
	 * @return 'false' se o custo atual atingir 120% do valor do orçamento, ou 'true' caso contrário
	 */
	public boolean verificaCusto() {
		if(custoAtual > orcamento.getPrecoPrevisto() * 1.2) return false;
		return true;
	}

	// ****** Mudar Estado ******

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

			//Guarda o tempo utilizado para executar o passo atual
			if(passos.size() > 0)
				passos.get(passos.size() - 1).addTempo(Horas.converteTimeMillisParaHoras(System.currentTimeMillis() - inicioPassoAtual));

			setEstado(estado);
			return true;
		}
		return false;
	}

	private boolean retomarServico(EstadoServico estado){
		if(estado == EstadoServico.EmExecucao){

			//Volta a contar o tempo
			inicioPassoAtual = System.currentTimeMillis();

			setEstado(estado);
			return true;
		}
		return false;
	}

	/**
	 * @return float indicando o tempo gasto, para realizar os passos, até ao passo atual.
	 */
	public float duracaoPassos(){
		float tempo = 0;
		for(Passo p : passos) tempo += p.getTempo();
		return tempo;
	}

	/**
	 * @return float indicando o tempo previsto, para realizar todos passos.
	 */
	public float duracaoPassosPrevistos(){
		return orcamento.getTempoPrevisto();
	}


	// ****** uxiliares ******

	@Override
	public int compareTo(Object o) {
		if(o instanceof Servico){
			return getDataConclusao().compareTo(((Servico) o).getDataConclusao());
		}
		return -1;
	}
}