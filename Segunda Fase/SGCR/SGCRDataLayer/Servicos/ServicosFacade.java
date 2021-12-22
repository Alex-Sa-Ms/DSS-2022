package SGCRDataLayer.Servicos;

import java.util.*;
import java.util.stream.Collectors;

public class ServicosFacade {

	Map<EstadoServico,Map<String,Servico>> estados;
	List<Servico> arquivados;
	private Deque<String> filaServicos;

	/**
	 * 
	 * @param idEquip
	 * @param custo
	 */
	public boolean addServicoExpresso(String idCliente, String idEquip, float custo) {
		// TODO - implement ServicosFacade.addServicoExpresso
		throw new UnsupportedOperationException();
	}

	public List<Servico> getServicos() {
		List<Servico> servicos = new ArrayList<>();
		for (Map<String,Servico> m:this.estados.values()){
			for (Servico s: m.values()){
				servicos.add(s.clone());
			}
		}
		return servicos;
	}
	/**
	 * 
	 * @param idEquip
	 * @param descricao
	 * @param passos
	 */
	public boolean addServicoPadrao(String idCliente, String idEquip, String descricao, List<Passo> passos) {
		// TODO - implement ServicosFacade.addServicoPadrao
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Servico getServico(String id) {
		// TODO - implement ServicosFacade.getServico
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public List<Servico> listarServicosConcluidos(String idTecnico) {
		// TODO - implement ServicosFacade.listarServicosConcluidos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idEquip
	 */
	public boolean orcamentoAceite(String idEquip) {
		// TODO - implement ServicosFacade.orcamentoAceite
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idEquip
	 */
	public boolean orcamentoRejeitado(String idEquip) {
		// TODO - implement ServicosFacade.orcamentoRejeitado
		throw new UnsupportedOperationException();
	}

	public void arquivaServicos() {
		// TODO - implement ServicosFacade.arquivaServicos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idEquip
	 */
	public float getPrecoServico(String idEquip) {
		// TODO - implement ServicosFacade.getPrecoServico
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idEquip
	 */
	public boolean entregaEquipamento(String idEquip) {
		// TODO - implement ServicosFacade.entregaEquipamento
		throw new UnsupportedOperationException();
	}

	public Map<String, TreeSet<Servico>> listaIntervencoes() {
		// TODO - implement ServicosFacade.listaIntervencoes
		throw new UnsupportedOperationException();
	}

	public List<Servico> listaPedidosPendentes(){
		//TODO
		throw new UnsupportedOperationException();
	}

	public boolean addServicoPadraoIrreparavel(String idEquip, String descricao){
		//TODO
		throw new UnsupportedOperationException();
	}

	public Servico getProxServico(String idTecnico){ //todo alex, meu corno, achos que esta merda alguma vez retorna bolean?
		//TODO                                       //ja alterei aqui mas altera no diagrama de classes
		throw new UnsupportedOperationException();
	}

	public boolean addPasso(String ID, Passo passo, int index){ //TODO adiciona ao diagrama de classes
		//TODO
		throw new UnsupportedOperationException();
	}

	public boolean interrompeServico(String IDServico){ //TODO adiciona ao diagrama de classes
		//TODO
		throw new UnsupportedOperationException();
	}

	public boolean concluiServico(String IDServico){ //TODO adiciona ao diagrama de classes
		//TODO
		throw new UnsupportedOperationException();
	}

	public boolean retomaServico(String IDServico){ //TODO adiciona ao diagrama de classes
		//TODO
		throw new UnsupportedOperationException();
	}

}