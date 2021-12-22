package SGCRDataLayer.Funcionarios;

import java.util.List;
import java.util.Map;

public class FuncionarioFacade {

	Map<String,Funcionario> funcionarios;

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addTecnico(String id, String password) {
		// TODO - implement FuncionarioFacade.addTecnico
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addFuncBalcao(String id, String password) {
		// TODO - implement FuncionarioFacade.addFuncBalcao
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Funcionario getFuncionario(String id) {
		// TODO - implement FuncionarioFacade.getFuncionario
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	//-1 login incorreto
	// 0 funcionario balcao
	// 1 tecnico
	// 2 gestor
	public int verificaCredenciais(String id, String password) {
		// TODO - implement FuncionarioFacade.verificaCredenciais
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrRececoes(String idFuncBalcao) {
		// TODO - implement FuncionarioFacade.incNrRececoes
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrEntregas(String idFuncBalcao) {
		// TODO - implement FuncionarioFacade.incNrEntregas
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idTecnico
	 * @param idServico
	 */
	public boolean addServicoTecnico(String idTecnico, String idServico) {
		// TODO - implement FuncionarioFacade.addServicoTecnico
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idTecnico
	 * @param duracao
	 * @param duracaoPrevista
	 */
	public void incNrRepProgConcluidas(String idTecnico, float duracao, float duracaoPrevista) {
		// TODO - implement FuncionarioFacade.incNrRepProgConcluidas
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public void incNrRepExpConcluidas(String idTecnico) {
		// TODO - implement FuncionarioFacade.incNrRepExpConcluidas
		throw new UnsupportedOperationException();
	}

	public List<Tecnico> listarTecnicos() {
		// TODO - implement FuncionarioFacade.listarTecnicos
		throw new UnsupportedOperationException();
	}

	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		// TODO - implement FuncionarioFacade.listarFuncionariosBalcao
		throw new UnsupportedOperationException();
	}

	public Map<String, Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Map<String, Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public List<String> listarServicosTecnico(String idTecnico) {
		// TODO - implement FuncionarioFacade.listarServicosTecnico
		throw new UnsupportedOperationException();
	}

}