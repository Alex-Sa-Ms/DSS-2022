package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ServicosFacade implements iServico {

	private final Map<EstadoServico,Map<String,Servico>> estados;
	private final Map<String,Servico> arquivados;
	private final Deque<String> filaServicos;
	private final ReentrantLock servicoslock = new ReentrantLock();

	public ServicosFacade(){
		estados      = new HashMap<>();
		arquivados   = new HashMap<>();
		filaServicos = new ArrayDeque<>();

		for(EstadoServico e : EstadoServico.values())
			estados.put(e, new HashMap<>());
	}


	/**
	 * Atribui o preco por hora global dos servicos programados.
	 * @param precoHora Preço por hora
	 * @return true se foi possível alterar o preco por hora.
	 */
	public boolean setPrecoHora(float precoHora){
		return Passo.setPrecoHora(precoHora);
	}

	/**
	 * Cria um servico expresso, e coloca-o no inicio da fila de servicos em espera de reparacao.
	 * Dado que é suposto existir disponibilidade para executar o servico naquele momento, nao se tem em consideracao outros servicos expressos
	 * que tenham sido aceites "ao mesmo tempo".
	 * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
	 * @param idCliente Identificador do cliente
	 * @param custo Custo fixo do servico
	 */
	public boolean addServicoExpresso(String idEquip, String idCliente, float custo, String descricao) {
		try {
			servicoslock.lock();
			if(getApontadorServico(idEquip) != null) return false;
			filaServicos.addFirst(idEquip);
			estados.get(EstadoServico.EsperandoReparacao).put(idEquip, new ServicoExpresso(idEquip, idCliente, custo, descricao));
			return true;
		}
		finally {
			servicoslock.unlock();
		}
	}

	/**
	 * Cria um servico padrao, num estado em que aguarda pela confirmação do orçamento, por parte do cliente.
	 * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
	 * @param idCliente Identificador do cliente
	 * @param passos Lista de passos que se prevê que constituam o servico
	 * @param descricao Descricao que deve estar presente no orcamento
	 * @param prazoMaximo Data máxima para a qual se espera que o serviço esteja concluido
	 */
	public boolean addServicoPadrao(String idEquip, String idCliente, List<Passo> passos, String descricao, LocalDateTime prazoMaximo) {
		try {
			servicoslock.lock();
			if(getApontadorServico(idEquip) != null) return false;
			estados.get(EstadoServico.AguardarConfirmacao).put(idEquip, new ServicoPadrao(idEquip, idCliente, passos, descricao, prazoMaximo));
			return true;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Regista servico cujo equipamento foi declarado irreparavel pelo tecnico.
 	 * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
	 * @param idCliente Identificador do cliente
	 * @param idTecnico Identificador do técnico que indicou que o equipamento é irreparavel
	 * @param descricao Descricao do problema do equipamento
	 */
	public boolean addServicoPadraoIrreparavel(String idEquip, String idCliente, String idTecnico, String descricao){
		try {
			servicoslock.lock();
			if(getApontadorServico(idEquip) != null) return false;
			estados.get(EstadoServico.Irreparavel).put(idEquip, new ServicoPadrao(idEquip, idCliente, idTecnico, descricao));
			return true;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Procura o servico com o identificador fornecido, tanto nos servicos arquivados, como nao arquivados
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Clone do servico que possui o identificador fornecido
	 */
	public Servico getServico(String id) {
		try {
			servicoslock.lock();

			Servico servico = getApontadorServico(id);

			if(servico != null) return servico.clone();

			return null;
		}
		finally {
			servicoslock.unlock();
		}
	}

	/**
	 * Procura o servico nao arquivado com o identificador fornecido.
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Clone do servico que possui o identificador fornecido
	 */
	public Servico getServicoNaoArquivado(String id) {
		try {
			servicoslock.lock();

			Servico servico = getApontadorServicoNaoArquivado(id);

			if(servico != null) return servico.clone();

			return null;
		}
		finally {
			servicoslock.unlock();
		}
	}

	/**
	 * Procura o servico nao arquivado com o identificador fornecido.
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Clone do servico que possui o identificador fornecido
	 */
	public Servico getServicoArquivado(String id) {
		try {
			servicoslock.lock();

			Servico servico = getApontadorServicoArquivado(id);

			if(servico != null) return servico.clone();

			return null;
		}
		finally {
			servicoslock.unlock();
		}
	}

	/**
	 * Atualiza estado do proximo servico a ser executado para "Em execucao",
	 * e adiciona-lhe a informacao do tecnico que o vai executar.
	 * @param idTecnico Identificador do Técnico
	 * @return proximo servico a ser executado
	 */
	public Servico getProxServico(String idTecnico){
		try {
			servicoslock.lock();

			Servico servico;
			String idServico = filaServicos.poll();

			if(idServico != null && (servico = mudaEstado(EstadoServico.EsperandoReparacao, EstadoServico.EmExecucao, idServico)) != null) {
				servico.setIdTecnico(idTecnico);
				return servico.clone();
			}

			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Esperando Reparacao",
	 * e adiciona-o no fim da lista de servicos à espera de reparacao.
	 * @param idEquip Identificador do equipamento
	 */
	public boolean orcamentoAceite(String idEquip) {
		try {
			servicoslock.lock();

			if(mudaEstado(EstadoServico.AguardarConfirmacao, EstadoServico.EsperandoReparacao, idEquip) == null) return false;
			filaServicos.addLast(idEquip);
			return true;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Orcamento Recusado".
	 * @param idEquip Identificador do equipamento
	 */
	public boolean orcamentoRejeitado(String idEquip) {
		if(mudaEstado(EstadoServico.AguardarConfirmacao, EstadoServico.OrcamentoRecusado, idEquip) == null) return false;
		return true;
	}

	/**
	 * Muda o estado de um servico, com o identificador fornecido, que esta a ser executado para irreparavel.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como irreparavel. 'true' caso contrário
	 */
	private boolean orcamentoExpirado(String idServico){
		if(mudaEstado(EstadoServico.AguardarConfirmacao, EstadoServico.Expirado, idServico) == null) return false;
		return true;
	}

	/**
	 * Interrompe servico que possui o identificador fornecido.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser interrompido. 'true' caso contrário
	 */
	public boolean interrompeServico(String idServico){
		if(mudaEstado(EstadoServico.EmExecucao, EstadoServico.Interrompido, idServico) == null) return false;
		return true;
	}

	/**
	 * Retoma o servico que possui o identificador fornecido, i.e., volta a marcá-lo como "Em execucao".
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser retomado. 'true' caso contrário
	 */
	public boolean retomaServico(String idServico){
		if(mudaEstado(EstadoServico.Interrompido, EstadoServico.EmExecucao, idServico) == null) return false;
		return true;
	}

	/**
	 * Marca o servico que possui o identificador fornecido como concluido.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como concluido. 'true' caso contrário
	 */
	public boolean concluiServico(String idServico){
		if(mudaEstado(EstadoServico.EmExecucao, EstadoServico.Concluido, idServico) == null) return false;
		return true;
	}

	/**
	 * Muda o estado de um servico, com o identificador fornecido, que esta a ser executado para irreparavel.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como irreparavel. 'true' caso contrário
	 */
	public boolean servicoIrreparavel(String idServico){
		if(mudaEstado(EstadoServico.EmExecucao, EstadoServico.Irreparavel, idServico) == null) return false;
		return true;
	}

	/**
	 * Adiciona um passo a seguir ao passo atual.
	 * @param idServico Identificador do servico, ao qual se pretende adicionar o passo
	 * @param passo Passo que se pretende adicionar
	 * @return 'false' se o servico nao existir, ou se nao estiver a ser executado. 'true' caso contrário.
	 */
	public boolean addPasso(String idServico, Passo passo){
		try {
			servicoslock.lock();

			Servico servico = getApontadorServico(idServico);

			if(!(servico instanceof ServicoPadrao) || servico.getEstado() != EstadoServico.EmExecucao) return false;

			((ServicoPadrao) servico).addPasso(passo.getCustoPecas(), passo.getDescricao());

			return true;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Retorna o passo que está a ser executado no momento.
	 * @param idServico Identificador do servico
	 * @return Retorna o passo que está a ser executado no momento.
	 */
	public Passo getPassoAtual(String idServico){
		try {
			servicoslock.lock();

			Servico servico = getApontadorServico(idServico);

			return (servico instanceof ServicoPadrao) ? ((ServicoPadrao) servico).getPassoAtual() : null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * @param idServico Identificador do servico do qual se pretende o proximmo passo
	 * @return proximo passo a ser executado no servico
	 */
	public Passo proxPasso(String idServico) throws ServicoPadrao.CustoExcedidoException {
		try {
			servicoslock.lock();

			Servico servico = getApontadorServico(idServico);

			if(servico instanceof ServicoPadrao)
				return ((ServicoPadrao) servico).proxPasso();

			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Procura o servico com o identificador fornecido, e caso seja encontrado devolve o custo final deste
	 * ou o custo atual caso o serviço seja do tipo "Padrao" e esteja a ser executado de momento. No caso em que o servico nao é encontrado, devolve 0.
	 * @param idEquip Identificador do Equipamento
	 * @return float que corresponde ao custo do serviço.
	 */
	public float getPrecoServico(String idEquip) {
		try {
			servicoslock.lock();
			Servico servico = getApontadorServico(idEquip);
			if(servico != null) return servico.getCusto();
			return 0;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Para o metodo suceder, o servico com o identificador fornecido deve existir
	 * e encontrar-se num dos seguintes estados: 'Irreparavel', 'OrcamentoRecusado' e 'Concluido'.
	 * @param idEquip Identificador do equipamento (coincide com o de servico)
	 * @return -1 se nao existir um equipamento com este id para ser entregue, ou o valor a ser pago no ato de entrega.
	 */
	public float entregaEquipamento(String idEquip) {
		try {
			servicoslock.lock();

			Servico servico;

			//Procura nos servicos cujo equipamento foi considerado irreparavel
			//Neste caso, se for determinado irreparavel o custo deve ser 0
			if(arquivaServico(EstadoServico.Irreparavel, idEquip) != null)
				return 0;

			//Procura nos servicos concluidos e nos servicos cujo orcamento foi recusado
			if((servico = arquivaServico(EstadoServico.Concluido, idEquip)) != null ||
					(servico = arquivaServico(EstadoServico.OrcamentoRecusado, idEquip)) != null)
				return servico.getCusto();

			return -1;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Lista todos os tecnicos. Cada tecnico é acompanhado de um TreeSet com todos os servicos efetuados (EstadoServico igual a 'Concluido') por ele,
	 * ordenados por pelo mais recente ate o mais antigo.
	 * @return map com todos os tecnicos e os servicos associados a eles em TreeSets
	 */
	public Map<String, TreeSet<Servico>> listaIntervencoes() {
		try {
			servicoslock.lock();

			Map<String, TreeSet<Servico>> map = new HashMap<>();
			TreeSet<Servico> treeSet;

			//Distribui todos os servicos concluidos que ainda nao foram arquivados
			for(Servico servico : estados.get(EstadoServico.Concluido).values()){

				//Cria um TreeSet de servicos para o tecnico, caso ainda nao exista
				if((treeSet = map.get(servico.getIdTecnico())) == null) {
					treeSet = new TreeSet<>(Comparator.reverseOrder());
					map.put(servico.getIdTecnico(), treeSet);
				}

				//Guarda um clone do objeto servico no treeset
				treeSet.add(servico.clone());
			}

			//Distribui todos os servicos concluidos arquivados
			for(Servico servico : arquivados.values()){

				//Cria um TreeSet de servicos para o tecnico, caso ainda nao exista
				if((treeSet = map.get(servico.getIdTecnico())) == null) {
					treeSet = new TreeSet<>(Comparator.reverseOrder());
					map.put(servico.getIdTecnico(), treeSet);
				}

				//Guarda um clone do objeto servico no treeset
				treeSet.add(servico.clone());
			}

			return map;
		}
		finally { servicoslock.unlock(); }
	}

	/** @return lista de pedidos em espera de confirmacao */
	public List<Servico> listaServicosEmEsperaDeConfirmacao(){
		try {
			servicoslock.lock();
			return estados.get(EstadoServico.AguardarConfirmacao).values()
						  .stream()
						  .map(Servico::clone)
						  .collect(Collectors.toList());
		}
		finally { servicoslock.unlock(); }
	}

	/** @return lista de pedidos pendentes(em espera de reparacao). */
	public List<Servico> listaServicosPendentes(){
		try {
			servicoslock.lock();
			return estados.get(EstadoServico.EsperandoReparacao).values()
						  .stream()
						  .map(Servico::clone)
						  .collect(Collectors.toList());
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Procura nos servicos não arquivados, aqueles cuja Data de Conclusao do servico remarque a pelo menos 90 dias atras,
	 * sinalizando os equipamentos como abandonados, e posteriormente arquiva os respetivos servicos.
	 * Procura tambem por aqueles cuja data do orcamento, remarque a pelo menos 30 dias atras, marcando este servico como expirado.
	 */
	public void arquiva_e_sinalizaExpirados() {
		try {
			servicoslock.lock();
			Iterator<Map.Entry<String,Servico>> it;

			//Procura nos servicos concluidos
			it = estados.get(EstadoServico.Concluido).entrySet().iterator();
			auxiliarArquivaServicos(it);

			//Procura nos servicos cujo orcamento foi recusado
			it = estados.get(EstadoServico.OrcamentoRecusado).entrySet().iterator();
			auxiliarArquivaServicos(it);

			//Procura nos servicos cujo equipamento foi considerado irreparavel
			it = estados.get(EstadoServico.Irreparavel).entrySet().iterator();
			auxiliarArquivaServicos(it);

			//Procura nos servicos cujo orcamento expirou
			it = estados.get(EstadoServico.Expirado).entrySet().iterator();
			auxiliarArquivaServicos(it);

			//Procura nos servicos que se encontram no estado "A aguardar confirmacao",
			//e marca como expirados aqueles cuja data presente no orcamento seja de há pelo menos 30 dias
			LocalDateTime now = LocalDateTime.now();
			for(Map.Entry<String,Servico> entry : estados.get(EstadoServico.AguardarConfirmacao).entrySet()){
				if(ChronoUnit.DAYS.between(((ServicoPadrao) entry.getValue()).getDataOrcamento(), now) >= 30)
					orcamentoExpirado(entry.getKey());
			}
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Calcula a data na qual se espera que o serviço esteja concluído, tendo em conta o número de técnicos e a duração prevista do serviço a efetuar.
	 * @param nrTecnicos Número de técnicos do centro
	 * @param duracaoPrevistaServico Duração prevista do serviço a efetuar
	 * @return data na qual se espera que o serviço esteja concluído
	 */
	public LocalDateTime calculaPrazoMaximo(int nrTecnicos, float duracaoPrevistaServico){
		int nrServicos;
		float tempoMax;
		float tempoMedioPrevisto;

		try {
			servicoslock.lock();

			//Calcula o tempo, aproximado, máximo para todos os tecnicos acabarem o servico atual
			tempoMax = 0;
			for(Servico s : estados.get(EstadoServico.EmExecucao).values()) {

				if(s instanceof ServicoExpresso && tempoMax < 30)
					tempoMax = 30;
				else if(s instanceof ServicoPadrao){
					float duracaoPrevista = ((ServicoPadrao) s).duracaoPassosPrevistos();
					if(tempoMax < duracaoPrevista) tempoMax = duracaoPrevista;
				}

			}

			//Calcula media de tempo prevista para todos os servicos à espera de serem executados
			tempoMedioPrevisto = 0;
			Map<String,Servico> mapServicos = estados.get(EstadoServico.EsperandoReparacao);
			nrServicos = mapServicos.size();

			if(nrServicos != 0) {

				for (Servico s : mapServicos.values()) {
					if (s instanceof ServicoExpresso)
						tempoMedioPrevisto += 30;
					else if (s instanceof ServicoPadrao)
						tempoMedioPrevisto += ((ServicoPadrao) s).duracaoPassosPrevistos();
				}

				tempoMedioPrevisto = tempoMedioPrevisto / nrServicos;
			}

		}
		finally { servicoslock.unlock(); }

		//"Divide" os servicos pelos tecnicos existentes e calcula o tempo, aproximado, necessário para todos os concluirem
		float tempoMaxServicosAguardandoReparacao = ((float) nrServicos / nrTecnicos) * tempoMedioPrevisto;

		//Calculo final, aproximado, do número máximo de minutos de trabalho necessários para o servico começar a ser executado por um técnico
		int tempoMinutos = (int) ((tempoMax + tempoMaxServicosAguardandoReparacao + duracaoPrevistaServico)
								* (float) 1.25); // adicionada uma margem de 25%

		//Calculo das horas
		int tempoHoras = tempoMinutos / 60;
		tempoMinutos   = tempoMinutos % 60;

		//Calculo dos dias
		//Admitido um número maximo de 6 horas de trabalho por dia.
		int tempoDays = tempoHoras / 6;
		tempoHoras    = tempoHoras % 6;

		return LocalDateTime.now().plusMinutes((long) tempoMinutos).plusHours((long) tempoHoras).plusDays((long) tempoDays);
	}

	// ****** Auxiliares ******

	/**
	 * Procura o servico com o identificador fornecido, tanto nos servicos arquivados, como nao arquivados
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Apontador para o servico que possui o identificador fornecido
	 */
	private Servico getApontadorServico(String id){
		try {
			servicoslock.lock();
			Servico servico = null;

			//Procura nos servicos não arquivados
			for(Map<String,Servico> e : estados.values())
				if ((servico = e.get(id)) != null) return servico;

			//Procura nos servicos arquivados
			if ((servico = arquivados.get(id)) != null) return servico;

			//Não encontrou o servico
			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Procura por um servico nao arquivado com o identificador fornecido
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Apontador para o servico que possui o identificador fornecido
	 */
	private Servico getApontadorServicoNaoArquivado(String id){
		try {
			servicoslock.lock();
			Servico servico = null;

			//Procura nos servicos não arquivados
			for(Map<String,Servico> e : estados.values())
				if ((servico = e.get(id)) != null) return servico;

			//Não encontrou o servico
			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Procura por um servico arquivado com o identificador fornecido
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Apontador para o servico que possui o identificador fornecido
	 */
	private Servico getApontadorServicoArquivado(String id){
		try {
			servicoslock.lock();
			Servico servico = null;

			//Procura nos servicos arquivados
			if ((servico = arquivados.get(id)) != null) return servico;

			//Não encontrou o servico
			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Arquiva o servico que esteja no estado de servico fornecido.
	 * Nao marca o equipamento como abandonado.
	 * @param es Estado do Servico
	 * @param idEquip Identificador do equipamento (coincide com o de servico)
	 */
	private Servico arquivaServico(EstadoServico es, String idEquip){
		try {
			servicoslock.lock();
			Servico servico = estados.get(es).remove(idEquip);
			if(servico != null){
				arquivados.put(servico.getId(), servico);
				return servico;
			}
			return null;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Muda um servico de estado.
	 * @param estadoAtual Estado atual do servico
	 * @param estadoPretendido Estado para o qual se pretende mudar o servico
	 * @param idServico Identificador do servico
	 * @return o Servico com o identificador fornecido caso a mudanca de estado tenha sido efetuada, ou 'null' caso contrario
	 */
	private Servico mudaEstado(EstadoServico estadoAtual, EstadoServico estadoPretendido, String idServico){
		try {
			servicoslock.lock();

			Servico servico = estados.get(estadoAtual).remove(idServico);

			if(servico == null || !servico.mudaEstado(estadoPretendido)) return null;

			estados.get(estadoPretendido).put(servico.getId(), servico);
			return servico;
		}
		finally { servicoslock.unlock(); }
	}

	/**
	 * Auxiliar do método 'arquiva_e_sinalizaExpirados'. Percorre um map com entradas de servicos, removendo
	 * e arquivando aqueles cuja data de conclusao se distancia em 90 dias da data atual
	 * @param it Iterador necessário para percorrer o map
	 */
	private void auxiliarArquivaServicos(Iterator<Map.Entry<String,Servico>> it){
		try {
			servicoslock.lock();

			Map.Entry<String,Servico> entry;
			Servico servico;
			LocalDateTime now = LocalDateTime.now();

			while (it.hasNext()) {
				entry = it.next();
				servico = entry.getValue();

				if(ChronoUnit.DAYS.between(servico.getDataConclusao(), now) >= 90){
					servico.setAbandonado(true);
					arquivados.put(entry.getKey(), servico);
					it.remove();
				}
			}
		}
		finally { servicoslock.unlock(); }
	}

	//TODO - remover no fim de tudo estar pronto

	@Override
	public String toString() {
		return "ServicosFacade{" +
				"estados=" + estados +
				", arquivados=" + arquivados +
				", filaServicos=" + filaServicos +
				'}';
	}
}