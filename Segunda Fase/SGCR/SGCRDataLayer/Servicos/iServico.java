package SGCRDataLayer.Servicos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

interface iServico extends Serializable{

    /**
     * Atribui o preco por hora global dos servicos programados.
     * @param precoHora Preço por hora
     * @return true se foi possível alterar o preco por hora.
     */
    boolean setPrecoHora(float precoHora);

    /**
     * Cria um servico expresso, e coloca-o no inicio da fila de servicos em espera de reparacao.
     * Dado que é suposto existir disponibilidade para executar o servico naquele momento, nao se tem em consideracao outros servicos expressos
     * que tenham sido aceites "ao mesmo tempo".
     * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
     * @param idCliente Identificador do cliente
     * @param custo Custo fixo do servico
     */
    boolean addServicoExpresso(String idEquip, String idCliente, float custo, String descricao);

    /**
     * Cria um servico padrao, num estado em que aguarda pela confirmação do orçamento, por parte do cliente.
     * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
     * @param idCliente Identificador do cliente
     * @param passos Lista de passos que se prevê que constituam o servico
     * @param descricao Descricao que deve estar presente no orcamento
     * @param prazoMaximo Data máxima para a qual se espera que o serviço esteja concluido
     */
    boolean addServicoPadrao(String idEquip, String idCliente, List<Passo> passos, String descricao, LocalDateTime prazoMaximo);

    /**
     * Regista servico cujo equipamento foi declarado irreparavel pelo tecnico.
     * @param idEquip Identificador do equipamento, que vai servir de identificador do servico
     * @param idCliente Identificador do cliente
     * @param idTecnico Identificador do técnico que indicou que o equipamento é irreparavel
     * @param descricao Descricao do problema do equipamento
     */
    boolean addServicoPadraoIrreparavel(String idEquip, String idCliente, String idTecnico, String descricao);

    /**
     * Procura o servico com o identificador fornecido, tanto nos servicos arquivados, como nao arquivados
     * @param id Identificador do serviço que coincide com o id do equipamento
     * @return Clone do servico que possui o identificador fornecido
     */
    Servico getServico(String id);

    /**
     * Procura o servico nao arquivado com o identificador fornecido.
     * @param id Identificador do serviço que coincide com o id do equipamento
     * @return Clone do servico que possui o identificador fornecido
     */
    Servico getServicoNaoArquivado(String id);

    /**
     * Procura o servico nao arquivado com o identificador fornecido.
     * @param id Identificador do serviço que coincide com o id do equipamento
     * @return Clone do servico que possui o identificador fornecido
     */
    Servico getServicoArquivado(String id);

    /**
     * Atualiza estado do proximo servico a ser executado para "Em execucao",
     * e adiciona-lhe a informacao do tecnico que o vai executar.
     * @param idTecnico Identificador do Técnico
     * @return proximo servico a ser executado
     */
    Servico getProxServico(String idTecnico);

    /**
     * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Esperando Reparacao",
     * e adiciona-o no fim da lista de servicos à espera de reparacao.
     * @param idEquip Identificador do equipamento
     */
    boolean orcamentoAceite(String idEquip);

    /**
     * Caso o servico estiver no estado em que aguarda confirmacao do orcamento, altera o seu estado para "Orcamento Recusado".
     * @param idEquip Identificador do equipamento
     */
    boolean orcamentoRejeitado(String idEquip);

    /**
     * Interrompe servico que possui o identificador fornecido.
     * @param idServico Identificador do servico
     * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser interrompido. 'true' caso contrário
     */
    boolean interrompeServico(String idServico);

    /**
     * Retoma o servico que possui o identificador fornecido, i.e., volta a marcá-lo como "Em execucao".
     * @param idServico Identificador do servico
     * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser retomado. 'true' caso contrário
     */
    boolean retomaServico(String idServico);

    /**
     * Marca o servico que possui o identificador fornecido como concluido.
     * @param idServico Identificador do servico
     * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como concluido. 'true' caso contrário
     */
    boolean concluiServico(String idServico);

    /**
     * Muda o estado de um servico, com o identificador fornecido, que esta a ser executado para irreparavel.
     * @param idServico Identificador do servico
     * @return 'false' se nao existe um servico, com o identificador fornecido, que possa ser marcado como irreparavel. 'true' caso contrário
     */
    boolean servicoIrreparavel(String idServico);

    /**
     * Adiciona um passo a seguir ao passo atual.
     * @param idServico Identificador do servico, ao qual se pretende adicionar o passo
     * @param passo Passo que se pretende adicionar
     * @return 'false' se o servico nao existir, ou se nao estiver a ser executado. 'true' caso contrário.
     */
    boolean addPasso(String idServico, Passo passo);

    /**
     * Retorna o passo que está a ser executado no momento.
     * @param idServico Identificador do servico
     * @return Retorna o passo que está a ser executado no momento.
     */
    Passo getPassoAtual(String idServico);

    /**
     * @param idServico Identificador do servico do qual se pretende o proximmo passo
     * @return proximo passo a ser executado no servico
     */
    Passo proxPasso(String idServico) throws ServicoPadrao.CustoExcedidoException;

    /**
     * Procura o servico com o identificador fornecido, e caso seja encontrado devolve o custo final deste
     * ou o custo atual caso o serviço seja do tipo "Padrao" e esteja a ser executado de momento. No caso em que o servico nao é encontrado, devolve 0.
     * @param idEquip Identificador do Equipamento
     * @return float que corresponde ao custo do serviço.
     */
    float getPrecoServico(String idEquip);

    /**
     * Para o metodo suceder, o servico com o identificador fornecido deve existir
     * e encontrar-se num dos seguintes estados: 'Irreparavel', 'OrcamentoRecusado' e 'Concluido'.
     * @param idEquip Identificador do equipamento (coincide com o de servico)
     * @return -1 se nao existir um equipamento com este id para ser entregue, ou o valor a ser pago no ato de entrega.
     */
    float entregaEquipamento(String idEquip);

    /**
     * Lista todos os tecnicos. Cada tecnico é acompanhado de um TreeSet com todos os servicos efetuados (EstadoServico igual a 'Concluido') por ele,
     * ordenados por pelo mais recente ate o mais antigo.
     * @return map com todos os tecnicos e os servicos associados a eles em TreeSets
     */
    Map<String, TreeSet<Servico>> listaIntervencoes();

    /** @return lista de pedidos em espera de confirmacao */
    List<Servico> listaServicosEmEsperaDeConfirmacao();

    /** @return lista de pedidos pendentes(em espera de reparacao). */
    List<Servico> listaServicosPendentes();

    /**
     * Procura nos servicos não arquivados, aqueles cuja Data de Conclusao do servico remarque a pelo menos 90 dias atras,
     * sinalizando os equipamentos como abandonados, e posteriormente arquiva os respetivos servicos.
     * Procura tambem por aqueles cuja data do orcamento, remarque a pelo menos 30 dias atras, marcando este servico como expirado.
     */
    void arquiva_e_sinalizaExpirados();

    /**
     * Calcula a data na qual se espera que o serviço esteja concluído, tendo em conta o número de técnicos e a duração prevista do serviço a efetuar.
     * @param nrTecnicos Número de técnicos do centro
     * @param duracaoPrevistaServico Duração prevista do serviço a efetuar
     * @return data na qual se espera que o serviço esteja concluído
     */
    LocalDateTime calculaPrazoMaximo(int nrTecnicos, float duracaoPrevistaServico);
}