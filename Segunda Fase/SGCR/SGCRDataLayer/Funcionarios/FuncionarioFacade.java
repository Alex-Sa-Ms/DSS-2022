package SGCRDataLayer.Funcionarios;

import java.io.Serializable;
import java.util.*;

public class FuncionarioFacade implements Serializable {

	Map<String,Funcionario> funcionarios;

	public FuncionarioFacade() {
		funcionarios = new HashMap<>();
		funcionarios.put("Alfredo",new Gestor("Alfredo","12345"));
	}

	public FuncionarioFacade(Map<String, Funcionario> funcionarios) {
		this.funcionarios = funcionarios != null ? new HashMap<>(funcionarios) : new HashMap<>();
	}

	public int getNrTecnicos(){
		int nr = 0;
		for(Funcionario f : funcionarios.values())
			if(f instanceof Tecnico) nr++;
		return nr;
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addTecnico(String id, String password) {
		if(funcionarios.containsKey(id)) return false;
		funcionarios.put(id, new Tecnico(id,password));
		return funcionarios.containsKey(id);
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addFuncBalcao(String id, String password) {
		if(funcionarios.containsKey(id)) return false;
		funcionarios.put(id, new FuncionarioBalcao(id, password));
		return funcionarios.containsKey(id);
	}

	/**
	 * 
	 * @param id
	 */
	public Funcionario getFuncionario(String id) {
		Funcionario funcionario = funcionarios.get(id);
		if(funcionario != null) return funcionario.clone();
		return null;
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
		Funcionario funcionario = funcionarios.get(id);
		if(funcionario != null) {
			if(funcionario.getPassword().equals(password)) {
				if (funcionario instanceof FuncionarioBalcao) return 0;
				else if (funcionario instanceof Tecnico) return 1;
				else if (funcionario instanceof Gestor) return 2;
			}
		}
		return (-1);
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrRececoes(String idFuncBalcao) {
		Funcionario funcionario = funcionarios.get(idFuncBalcao);
		if(funcionario instanceof FuncionarioBalcao)
			((FuncionarioBalcao) funcionario).incNrRececoes();
		return false;
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrEntregas(String idFuncBalcao) {
		Funcionario funcionario = funcionarios.get(idFuncBalcao);
		if(funcionario instanceof FuncionarioBalcao)
			((FuncionarioBalcao) funcionario).incNrEntregas();
		return false;
	}

	/**
	 * 
	 * @param idTecnico
	 * @param idServico
	 */
	public boolean addServicoTecnico(String idTecnico, String idServico) {
		if(funcionarios.containsKey(idServico)) return false;
		((Tecnico) funcionarios.get(idTecnico)).addServico(idServico);
		return true;
	}

	/**
	 * 
	 * @param idTecnico
	 * @param duracao
	 * @param duracaoPrevista
	 */
	public void incNrRepProgConcluidas(String idTecnico, float duracao, float duracaoPrevista) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			((Tecnico) funcionario).incNrRepProgConcluidas(duracao,duracaoPrevista);
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public void incNrRepExpConcluidas(String idTecnico) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			((Tecnico) funcionario).incNrRepExpConcluidas();
	}

	public List<Tecnico> listarTecnicos() {
		List<Tecnico> tecs = new ArrayList<>();

		for(Funcionario func: funcionarios.values())
			if(func instanceof Tecnico)
				tecs.add(((Tecnico) func).clone());

		return tecs;
	}

	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		List<FuncionarioBalcao> fb = new ArrayList<>();

		for(Funcionario func: funcionarios.values())
			if(func instanceof FuncionarioBalcao)
				fb.add(((FuncionarioBalcao) func).clone());

		return fb;
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public List<String> listarServicosTecnico(String idTecnico) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			return ((Tecnico) funcionario).getServicos();
		return null;
	}

	public boolean possuiServico(String idTecnico, String idServico){
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico) return ((Tecnico) funcionario).possuiServico(idServico);
		return false;
	}
}