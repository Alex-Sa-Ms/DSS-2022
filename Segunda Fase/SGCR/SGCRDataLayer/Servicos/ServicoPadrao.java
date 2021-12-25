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

	//TODO - mudar Tempo.converteTimeMillisParaSegundos -> Tempo.converteTimeMillisParaHoras

	// ****** Construtores ******

	/**
	 * Método auxiliar dos construtores. Inicializa as variáveis de instancia partilhadas por todos os construtores publicos.
	 * @param id Identificador pretendido para o servico
	 * @param idCliente Identificador do cliente
	 * @param passosOrcamento Lista de passos para o orcamento
	 * @param descricaoOrcamento Descricao do problema que levou à requisicao do servico
	 */
	private void AuxiliarConstrutor(String id, String idCliente, List<Passo> passosOrcamento, String descricaoOrcamento){
		setId(id);
		setIdCliente(idCliente);
		setAbandonado(false);
		passos 				= new ArrayList<>();
		orcamento 			= new Orcamento(passosOrcamento, descricaoOrcamento);
		custoAtual 			= 0;
		inicioPassoAtual	= 0;
		passoAtual          = -1; //-1 para sincronizar com o método 'proxPasso'
		passoAtualOrcamento = -1; //-1 para sincronizar com o método 'proxPasso'
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
		setIdTecnico(null); //É alterado depois aquando da atribuicao do servico a um técnico
		setEstado(EstadoServico.AguardarConfirmacao);
		setDataConclusao(null);
	}

	/**
	 * ServicoPadrao iniciado com o Técnico a null. Apenas lhe é atribuido o técnico posteriormente por meio de um setter
	 * @param id Identificador pretendido para o serviço
	 * @param idTecnico Identificador do técnico responsável pela execucao do servico
	 * @param idCliente Identificador do cliente que requisitou o pedido
	 * @param descricaoOrcamento Descricao do problema, pelo qual é necessário a execução do servico de reparacao
	 */
	public ServicoPadrao(String id, String idCliente, String idTecnico, String descricaoOrcamento) {
		AuxiliarConstrutor(id, idCliente, null, descricaoOrcamento);
		setIdTecnico(idTecnico);
		setEstado(EstadoServico.Irreparavel);
		setDataConclusao(LocalDateTime.now());
	}

	// ****** Clone ******

	/**
	 * Construtor utilizado para clonar um servico.
	 * @param sp ServicoPadrao que se pretende clonar.
	 */
	public ServicoPadrao(ServicoPadrao sp) {
		setEstado(sp.getEstado());
		setId(sp.getId());
		setIdTecnico(sp.getIdTecnico());
		setIdCliente(sp.getIdCliente());
		setAbandonado(sp.getAbandonado());
		setDataConclusao(sp.getDataConclusao());
		this.passos              = sp.getPassos();
		this.orcamento           = sp.getOrcamento();
		this.custoAtual          = sp.getCusto();
		this.passoAtualOrcamento = sp.passoAtualOrcamento;
		this.inicioPassoAtual    = sp.inicioPassoAtual;
		this.passoAtual			 = sp.passoAtual;
	}

	/**
	 * Utilizada para obter um clone de um servico.
	 * @return clone do servico
	 */
	@Override
	public Servico clone() { return new ServicoPadrao(this); }

	// ****** getters e setters ******

	/** @return lista de passos (clonados) de um servico. */
	public List<Passo> getPassos() { return passos.stream().map(Passo::clone).collect(Collectors.toList()); }

	/** @return lista de passos (clonados), pertencentes ao orcamento, de um servico. */
	public List<Passo> getPassosOrcamento() { return orcamento.listarPassosOrcamento(); }

	/** @return clone do orcamento do servico */
	public Orcamento getOrcamento() { return orcamento.clone(); }

	/** @return float que indica o custo do serviço até ao momento */
	public float getCusto() { return custoAtual; }

	//TODO - add diagrama
	/** @return data de criacao do orcamento **/
	public LocalDateTime getDataOrcamento() { return orcamento.getData(); }

	/**
	 * Adiciona um passo a seguir ao passo atual.
	 * Caso já tenha(m) sido adicionado(s) outro(s) passo(s),
	 * adiciona-o a seguir deste(s).
	 * O tempo do passo é inicializado a 0.
	 * @param custoPecas Custo das pecas utilizadas no passo
	 * @param descricao Descricao do passo
	 */
	public void addPasso(float custoPecas, String descricao) {
		passos.add(new Passo(custoPecas, descricao));
	}

	/** @return Clone do próximo 'Passo' a ser executado, ou 'null' caso não exista. */
	public Passo proxPasso() {
		//Guarda o tempo utilizado para executar o passo atual, e atualiza a variavel custoAtual, antes de saltar para o próximo passo
		Passo passo = getPassoAtualPrivate();
		if(passo != null) {
			// TODO - mudar Tempo.converteTimeMillisParaSegundos -> Tempo.converteTimeMillisParaHoras
			passo.addTempo(Tempo.converteTimeMillisParaSegundos(System.currentTimeMillis() - inicioPassoAtual));
			custoAtual += passo.getCustoPecas() + passo.getTempo() * passo.getPrecoHora();
		}

		passoAtual++;
		Passo novoPasso;

		//Verifica que se foi adicionado algum passo, durante a reparacao, pelo técnico
		if(passoAtual < passos.size())
			novoPasso = passos.get(passoAtual).clone();
		//Se nao foi, tenta ir buscar o proximo passo ao orcamento
		else {
			passoAtualOrcamento++;
			novoPasso = orcamento.getPasso(passoAtualOrcamento);
			if(novoPasso != null) {
				novoPasso.setTempo(0); //Elimina o tempo que vem no passo (clonado) do orçamento
				passos.add(novoPasso);
				novoPasso = novoPasso.clone();
			}
		}

		//Marca a data em que foi iniciada a execucao do passo
		inicioPassoAtual = System.currentTimeMillis();

		return novoPasso;
	}

	/** @return Passo em que o servico se encontra */
	private Passo getPassoAtualPrivate() {
		if(passoAtual >= 0 && passoAtual < passos.size())
			return passos.get(passoAtual);
		return null;
	}

	/** @return Passo (clonado) em que o servico se encontra */
	public Passo getPassoAtual() {
		Passo passo = getPassoAtualPrivate();
		if(passo != null) return passo.clone();
		return null;
	}

	/** @return 'false' se o custo atual atinge o patamar de 120% relativamente ao valor do orçamento, ou 'true' caso contrário */
	public boolean verificaCusto() {
		if(custoAtual > orcamento.getPrecoPrevisto() * 1.2) return false;
		return true;
	}

	// ****** Mudar Estado ******

	/**
	 * Método que apenas altera o estado do serviço para o estado fornecido, caso este cumpra a ordem estabelecida.
	 * @param estado Estado para o qual se pretende que o servico mude
	 * @return 'true' se o estado foi alterado com sucesso, ou 'false' caso contrário.
	 */
	@Override
	public boolean mudaEstado(EstadoServico estado) {
		EstadoServico estadoAtual = getEstado();
		if(estadoAtual == EstadoServico.AguardarConfirmacao) 	 return aceitarOuRejeitarOuExpiradoOrcamento(estado);
		else if(estadoAtual == EstadoServico.EsperandoReparacao) return executarServico(estado);
		else if(estadoAtual == EstadoServico.EmExecucao) 		 return interromperOuConcluirOuIrreparavel(estado);
		else if(estadoAtual == EstadoServico.Interrompido) 		 return retomarServico(estado);
		else return false;
	}

	//Método auxiliar do método 'mudaEstado'
	private boolean aceitarOuRejeitarOuExpiradoOrcamento(EstadoServico estado){
		if(estado == EstadoServico.OrcamentoRecusado || estado == EstadoServico.EsperandoReparacao || estado == EstadoServico.Expirado){
			setEstado(estado);
			return true;
		}
		return false;
	}

	//Método auxiliar do método 'mudaEstado'
	private boolean executarServico(EstadoServico estado) {
		if (estado == EstadoServico.EmExecucao){
			setEstado(estado);
			return true;
		}
		return false;
	}

	//Método auxiliar do método 'mudaEstado'
	private boolean interromperOuConcluirOuIrreparavel(EstadoServico estado){
		if(estado == EstadoServico.Irreparavel || estado == EstadoServico.Concluido || estado == EstadoServico.Interrompido){

			//Guarda o tempo utilizado para executar o passo atual
			Passo passo = getPassoAtualPrivate();
			//TODO - mudar Tempo.converteTimeMillisParaSegundos -> Tempo.converteTimeMillisParaHoras
			if(passo != null) passo.addTempo(Tempo.converteTimeMillisParaSegundos(System.currentTimeMillis() - inicioPassoAtual));

			setDataConclusao(LocalDateTime.now());
			setEstado(estado);
			return true;
		}
		return false;
	}

	//Método auxiliar do método 'mudaEstado'
	private boolean retomarServico(EstadoServico estado){
		if(estado == EstadoServico.EmExecucao){

			//Volta a contar o tempo
			inicioPassoAtual = System.currentTimeMillis();

			setEstado(estado);
			return true;
		}
		return false;
	}

	/** @return float indicando o tempo gasto, para realizar os passos, até ao passo atual. */
	public float duracaoPassos(){
		float tempo = 0;
		for(Passo p : passos) tempo += p.getTempo();
		return tempo;
	}

	/** @return float indicando o tempo previsto, para realizar todos passos. */
	public float duracaoPassosPrevistos(){
		return orcamento.getTempoPrevisto();
	}


	//TODO: Remover depois da app estar feita
	@Override
	public String toString() {
		return "ServicoPadrao{" +
				super.toString() +
				", passos=" + passos +
				//", orcamento=" + orcamento +
				//", custoAtual=" + custoAtual +
				//", passoAtual=" + passoAtual +
				//", passoAtualOrcamento=" + passoAtualOrcamento +
				//", inicioPassoAtual=" + inicioPassoAtual +
				'}';
	}
}