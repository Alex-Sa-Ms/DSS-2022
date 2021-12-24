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
		this.funcionarios = funcionarios;
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addTecnico(String id, String password) {
		funcionarios.put(id, new Tecnico(id,password));
		return funcionarios.containsKey(id);
	}

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public boolean addFuncBalcao(String id, String password) {
		funcionarios.put(id, new FuncionarioBalcao(id,password));
		return funcionarios.containsKey(id);
	}

	/**
	 * 
	 * @param id
	 */
	public Funcionario getFuncionario(String id) {

		if(funcionarios.get(id) instanceof Tecnico) return ((Tecnico) funcionarios.get(id)).clone();
		if(funcionarios.get(id) instanceof FuncionarioBalcao) return ((FuncionarioBalcao) funcionarios.get(id)).clone();

		return ((Gestor) funcionarios.get(id)).clone();
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
		if(funcionarios.containsKey(id)) {

			if(funcionarios.get(id).getPassword().equals(password)) {
				if (funcionarios.get(id) instanceof FuncionarioBalcao) return 0;
				if (funcionarios.get(id) instanceof Tecnico) return 1;
				if (funcionarios.get(id) instanceof Gestor) return 2;
			}
		}
		return (-1);
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrRececoes(String idFuncBalcao) {
		if(!funcionarios.containsKey(idFuncBalcao)) return false;
		((FuncionarioBalcao) funcionarios.get(idFuncBalcao)).incNrRececoes();
		return true;
	}

	/**
	 * 
	 * @param idFuncBalcao
	 */
	public boolean incNrEntregas(String idFuncBalcao) {
		if(!funcionarios.containsKey(idFuncBalcao)) return false;
		((FuncionarioBalcao) funcionarios.get(idFuncBalcao)).incNrEntregas();
		return true;
	}

	/**
	 * 
	 * @param idTecnico
	 * @param idServico
	 */
	public boolean addServicoTecnico(String idTecnico, String idServico) {
		if(!funcionarios.containsKey(idServico)) return false;
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
		((Tecnico) funcionarios.get(idTecnico)).incNrRepProgConcluidas(duracao,duracaoPrevista);
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public void incNrRepExpConcluidas(String idTecnico) {
		((Tecnico) funcionarios.get(idTecnico)).incNrRepExpConcluidas();
	}

	public List<Tecnico> listarTecnicos() {
		List<Tecnico> tecs = new ArrayList<>();

		for(Map.Entry<String, Funcionario> entry: funcionarios.entrySet()){
			if(entry.getValue() instanceof Tecnico) tecs.add(((Tecnico) entry.getValue()).clone());
		}
		return tecs;
	}

	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		List<FuncionarioBalcao> fb = new ArrayList<>();

		for(Map.Entry<String, Funcionario> entry: funcionarios.entrySet()){
			if(entry.getValue() instanceof FuncionarioBalcao) fb.add(((FuncionarioBalcao) entry.getValue()).clone());
		}
		return fb;
	}

	/**
	 * 
	 * @param idTecnico
	 */
	public List<String> listarServicosTecnico(String idTecnico) {
		List<String> serv = new ArrayList<>();
		((Tecnico) funcionarios.get(idTecnico)).getServicos().addAll(serv);
		return serv;
	}
}