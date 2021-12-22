package SGCRDataLayer.Servicos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ServicosFacade {

	private final Map<EstadoServico,Map<String,Servico>> estados;
	private final Map<String,Servico> arquivados;
	private final Deque<String> filaServicos;

	public ServicosFacade(){
		estados = new HashMap<>();
		arquivados = new HashMap<>();
		filaServicos = new ArrayDeque<>();

		for(EstadoServico e : EstadoServico.values())
			estados.put(e, new HashMap<>());
	}

	//TODO - calcular prazo maximo
	//TODO - necessario lock(s) para as estruturas de dados, devido ao uso do Timer

	/**
	 * Cria um servico expresso, e coloca-o no inicio da fila de servicos em espera de reparacao.
	 * Dado que é suposto existir disponibilidade para executar o servico naquele momento, nao se tem em consideracao outros servicos expressos
	 * que tenham sido aceites "ao mesmo tempo".
	 * @param idCliente Identificador do cliente
	 * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
	 * @param custo Custo fixo do servico
	 */
	public boolean addServicoExpresso(String idCliente, String idEquip, float custo) {
		filaServicos.addFirst(idEquip);
		estados.get(EstadoServico.EsperandoReparacao).put(idEquip, new ServicoExpresso(idEquip, idCliente, custo));
		return true;
	}

	/**
	 * Cria um servico padrao, num estado em que aguarda pela confirmação do orçamento, por parte do cliente.
	 * @param idCliente Identificador do cliente
	 * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
	 * @param descricao Descricao que deve estar presente no orcamento
	 * @param passos Lista de passos que se prevê que constituam o servico
	 */
	public boolean addServicoPadrao(String idCliente, String idEquip, String descricao, List<Passo> passos) {
		estados.get(EstadoServico.AguardarConfirmacao).put(idEquip, new ServicoPadrao(idEquip, idCliente, passos, descricao));
		return true;
	}

	/**
	 * Procura o servico com o identificador fornecido, tanto nos servicos arquivados, como nao arquivados
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Clone do servico que possui o identificador fornecido
	 */
	public Servico getServico(String id) {
		Servico servico = getApontadorServico(id);

		if(servico != null) return servico.clone();

		return null;
	}

	/**
	 * Procura o servico com o identificador fornecido, tanto nos servicos arquivados, como nao arquivados
	 * @param id Identificador do serviço que coincide com o id do equipamento
	 * @return Apontador para o servico que possui o identificador fornecido
	 */
	private Servico getApontadorServico(String id){
		Servico servico = null;

		//Procura nos servicos não arquivados
		for(Map<String,Servico> e : estados.values())
			if ((servico = e.get(id)) != null) return servico.clone();

		//Procura nos servicos arquivados
		if ((servico = arquivados.get(id)) != null) return servico.clone();

		//Não encontrou o servico
		return null;
	}

	//TODO - Que tipo de servicos devo fornecer neste metodos? Arquivados e nao arquivados em estado "Concluido"? Se é para fazer fornecam me a lista dos ids de servicos q tem no funcionariosFacade
	/**
	 * 
	 * @param idTecnico
	 */
	public List<Servico> listarServicosConcluidos(String idTecnico) {
		// TODO - implement ServicosFacade.listarServicosConcluidos
		throw new UnsupportedOperationException();
	}

	/**
	 * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Esperando Reparacao",
	 * e adiciona-o no fim da lista de servicos à espera de reparacao.
	 * @param idEquip Identificador do equipamento
	 */
	public boolean orcamentoAceite(String idEquip) {
		Servico servico = estados.get(EstadoServico.AguardarConfirmacao).remove(idEquip);

		if(servico == null) return false;

		servico.mudaEstado(EstadoServico.EsperandoReparacao);
		filaServicos.addLast(servico.getId());
		estados.get(EstadoServico.EsperandoReparacao).put(servico.getId(), servico);
		return true;
	}

	/**
	 * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Orcamento Recusado".
	 * @param idEquip Identificador do equipamento
	 */
	public boolean orcamentoRejeitado(String idEquip) {
		Servico servico = estados.get(EstadoServico.AguardarConfirmacao).remove(idEquip);

		if(servico == null) return false;

		servico.mudaEstado(EstadoServico.OrcamentoRecusado);
		estados.get(EstadoServico.OrcamentoRecusado).put(servico.getId(), servico);
		return true;
	}

	/**
	 * Procura nos servicos não arquivados, aqueles cuja Data de Conclusao do servico supere os 90 dias,
	 * sinalizando os equipamentos como abandonados, e posteriormente arquiva os respetivos servicos.
	 */
	public void arquivaServicos() {
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
	}

	/**
	 * Auxiliar do método 'arquivaServicos'. Percorre um map com entradas de servicos, removendo
	 * e arquivando aqueles cuja data de conclusao se distancia em 90 dias da data atual
	 * @param it Iterador necessário para percorrer o map
	 */
	private void auxiliarArquivaServicos(Iterator<Map.Entry<String,Servico>> it){
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

	/**
	 * Procura o servico com o identificador fornecido, e caso seja encontrado devolve o custo final deste
	 * ou o custo atual caso o serviço seja do tipo "Padrao" e esteja a ser executado de momento. No caso em que o servico nao é encontrado, devolve 0.
	 * @param idEquip Identificador do Equipamento
	 * @return float que corresponde ao custo do serviço.
	 */
	public float getPrecoServico(String idEquip) {
		Servico servico = getApontadorServico(idEquip);
		if(servico != null) return servico.getCusto();
		return 0;
	}

	/**
	 * Para o metodo suceder, o servico com o identificador fornecido deve existir
	 * e encontrar-se num dos seguintes estados: 'Irreparavel', 'OrcamentoRecusado' e 'Concluido'.
	 * @param idEquip Identificador do equipamento (coincide com o de servico)
	 * @return -1 se nao existir um equipamento com este id para ser entregue, ou o valor a ser pago no ato de entrega.
	 */
	public float entregaEquipamento(String idEquip) {
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

	/**
	 * Arquiva o servico que esteja no estado de servico fornecido.
	 * Nao marca o equipamento como abandonado.
	 * @param es Estado do Servico
	 * @param idEquip Identificador do equipamento (coincide com o de servico)
	 */
	private Servico arquivaServico(EstadoServico es, String idEquip){
		Servico servico = estados.get(es).remove(idEquip);
		if(servico != null){
			arquivados.put(servico.getId(), servico);
			return servico;
		}
		return null;
	}

	/**
	 * Lista todos os tecnicos. Cada tecnico é acompanhado de um TreeSet com todos os servicos efetuados (EstadoServico igual a 'Concluido') por ele,
	 * ordenados por pelo mais recente ate o mais antigo.
	 * @return map com todos os tecnicos e os servicos associados a eles em TreeSets
	 */
	public Map<String, TreeSet<Servico>> listaIntervencoes() {
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

	/**
	 * @return lista de pedidos pendentes, ou seja, em espera de reparacao.
	 */
	public List<Servico> listaPedidosPendentes(){
		return estados.get(EstadoServico.EsperandoReparacao).values()
															.stream()
														    .map(Servico::clone)
															.collect(Collectors.toList());
	}

	/**
	 * Regista servico cujo equipamento foi declarado irreparavel pelo tecnico.
	 */
	public boolean addServicoPadraoIrreparavel(String idCliente, String idTecnico, String idEquip, String descricao){
		estados.get(EstadoServico.Irreparavel).put(idEquip, new ServicoPadrao(idCliente, idTecnico, idEquip, descricao));
		return true;
	}

	/**
	 * Atualiza estado do proximo servico a ser executado para "Em execucao",
	 * e adiciona-lhe a informacao do tecnico que o vai executar.
	 * @param idTecnico Identificador do Técnico
	 * @return proximo servico a ser executado
	 */
	public Servico getProxServico(String idTecnico){
		String idServico = filaServicos.poll();

		if(idServico != null) {
			Servico servico = estados.get(EstadoServico.EsperandoReparacao).remove(idServico);
			estados.get(EstadoServico.EmExecucao).put(idServico, servico);
			servico.mudaEstado(EstadoServico.EmExecucao);
			servico.setIdTecnico(idTecnico);
			return servico.clone();
		}

		return null;
	}

	/**
	 * Adiciona um passo a seguir ao passo atual.
	 * @param idServico Identificador do servico, ao qual se pretende adicionar o passo
	 * @param passo Passo que se pretende adicionar
	 * @return 'false' se o servico nao existir, ou se nao estiver a ser executado. 'true' caso contrário.
	 */
	public boolean addPasso(String idServico, Passo passo){
		Servico servico = getApontadorServico(idServico);

		if(!(servico instanceof ServicoPadrao) || servico.getEstado() != EstadoServico.EmExecucao) return false;

		((ServicoPadrao) servico).addPasso(passo.getCustoPecas(), passo.getDescricao(), passo.getTempo());

		return true;
	}

	/**
	 * Interrompe servico que possui o identificador fornecido.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser interrompido. 'true' caso contrário
	 */
	public boolean interrompeServico(String idServico){
		Servico servico = estados.get(EstadoServico.EmExecucao).remove(idServico);

		if(servico == null) return false;

		servico.mudaEstado(EstadoServico.Interrompido);
		estados.get(EstadoServico.Interrompido).put(idServico, servico);
		return true;
	}

	/**
	 * Marca o servico que possui o identificador fornecido como concluido.
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como concluido. 'true' caso contrário
	 */
	public boolean concluiServico(String idServico){
		Servico servico = estados.get(EstadoServico.EmExecucao).remove(idServico);

		if(servico == null) return false;

		servico.mudaEstado(EstadoServico.Concluido);
		estados.get(EstadoServico.Concluido).put(idServico, servico);
		return true;
	}

	/**
	 * Retoma o servico que possui o identificador fornecido, i.e., volta a marcá-lo como "Em execucao".
	 * @param idServico Identificador do servico
	 * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser retomado. 'true' caso contrário
	 */
	public boolean retomaServico(String idServico){
		Servico servico = estados.get(EstadoServico.Interrompido).remove(idServico);

		if(servico == null) return false;

		servico.mudaEstado(EstadoServico.EmExecucao);
		estados.get(EstadoServico.EmExecucao).put(idServico, servico);
		return true;
	}

}