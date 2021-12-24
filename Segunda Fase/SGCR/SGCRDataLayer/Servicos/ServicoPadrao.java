package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import SGCRLogicLayer.Tempo;

public class ServicoPadrao extends Servico {

	private List<Passo> passos;
	private Orcamento orcamento;
	private float custoAtual;
	private int   passoAtual;
	private int   passoAtualOrcamento;
	private long  inicioPassoAtual;

	// ****** Construtores ******

	private void AuxiliarConstrutor(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento){
		setId(id);
		setIdCliente(idCliente);
		setAbandonado(false);
		passos 				= new ArrayList<>();
		orcamento 			= new Orcamento(passosOrcamento, descricaoOrcamento);
		custoAtual 			= 0;
		inicioPassoAtual	= 0;
		passoAtual          = -1;
		passoAtualOrcamento = -1;
	}

	/**
	 * Construtor ServicoPadrao para situação normal, i.e., orcamento feito, vai esperar pela resposta do cliente
	 * @param id Identificador pretendido para o serviço
	 * @param idCliente Identificador do cliente que requisitou o pedido
	 * @param passosOrcamento Lista com os passos que constituem o orçamento
	 * @param descricaoOrcamento Descricao do problema, pelo qual é necessário a execução do servico de reparacao
	 */
	public ServicoPadrao(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento) {
		AuxiliarConstrutor(id, idCliente, passosOrcamento, descricaoOrcamento);
		setIdTecnico(null); //É alterado depois de ser atribuido a um técnico
		setEstado(EstadoServico.AguardarConfirmacao);
		setDataConclusao(null);
	}

	/**
	 * ServicoPadrao iniciado com o Técnico a null. Apenas lhe é atribuido o técnico posteriormente por meio de um setter
	 * @param id Identificador pretendido para o serviço
	 * @param idTecnico
	 * @param idCliente Identificador do cliente que requisitou o pedido
	 * @param descricaoOrcamento
	 */
	public ServicoPadrao(String id, String idCliente, String idTecnico, String descricaoOrcamento) {
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
		this.inicioPassoAtual    = sp.inicioPassoAtual;
		this.passoAtual			 = sp.passoAtual;
	}

	@Override
	public Servico clone() {
		return new ServicoPadrao(this);
	}


	// ****** getters e setters ******

	public List<Passo> getPassos() { return passos.stream().map(Passo::clone).collect(Collectors.toList()); }

	public List<Passo> getPassosOrcamento() { return orcamento.listarPassosOrcamento(); }

	public Orcamento getOrcamento() { return orcamento.clone(); }

	/**
	 * @return float que indica o custo do serviço até ao momento
	 */
	public float getCusto() { return custoAtual; }

	private int getPassoAtualOrcamento() { return passoAtualOrcamento; }

	/**
	 * 
	 * @param custoPecas
	 * @param descricao
	 */
	public void addPasso(float custoPecas, String descricao) {
		passos.add(new Passo(custoPecas, descricao));
	}

	/**
	 * @return Próximo 'Passo' a ser executado, ou 'null' caso não exista.
	 */
	public Passo proxPasso() {
		//Guarda o tempo utilizado para executar o passo atual, e atualiza a variavel custoAtual, antes de saltar para o próximo passo
		Passo passo = getPassoAtualPrivate();
		if(passo != null) {
			passo.addTempo(Tempo.converteTimeMillisParaHoras(System.currentTimeMillis() - inicioPassoAtual));
			custoAtual += passo.getCustoPecas() + passo.getTempo() * passo.getPrecoHora();
		}

		passoAtual++;
		Passo novoPasso;

		//Verifica que se foi adicionado algum passo, durante a reparacao, pelo técnico
		if(passoAtual < passos.size())
			novoPasso = passos.get(passoAtual);
		//Se nao foi, tenta ir buscar o proximo passo ao orcamento
		else {
			passoAtualOrcamento++;
			novoPasso = orcamento.getPasso(passoAtualOrcamento);
			if(novoPasso != null) {
				novoPasso.setTempo(0); //Elimina o tempo que vem no passo do orçamento
				passos.add(novoPasso);
			}
		}

		//Marca a data em que o passo foi começado
		inicioPassoAtual = System.currentTimeMillis();

		return novoPasso;
	}

	/**
	 * Indica o passo atual em que o servico se encontra
	 * @return Passo atual clonado
	 */
	public Passo getPassoAtual() {
		Passo passo = getPassoAtualPrivate();
		if(passo != null) return passo.clone();
		return null;
	}

	/**
	 * Indica o passo atual em que o servico se encontra
	 * @return Passo atual não clonado
	 */
	public Passo getPassoAtualPrivate() {
		if(passoAtual >= 0 && passoAtual < passos.size())
			return passos.get(passoAtual);
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
			Passo passo = getPassoAtualPrivate();
			if(passo != null) passo.addTempo(Tempo.converteTimeMillisParaHoras(System.currentTimeMillis() - inicioPassoAtual));

			setDataConclusao(LocalDateTime.now());
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


	//TODO: Remover depois da app estar feita
	@Override
	public String toString() {
		return "ServicoPadrao{" +
				super.toString() +
				", passos=" + passos +
				", orcamento=" + orcamento +
				", custoAtual=" + custoAtual +
				", passoAtual=" + passoAtual +
				", passoAtualOrcamento=" + passoAtualOrcamento +
				", inicioPassoAtual=" + inicioPassoAtual +
				'}';
	}
}