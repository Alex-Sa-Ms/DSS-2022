package SGCRDataLayer.Funcionarios;

import java.util.ArrayList;
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
		Tecnico a = new Tecnico();
		FuncionarioBalcao b = new FuncionarioBalcao();
		if(funcionarios.get(id).getClass().equals(a.getClass())) return ((Tecnico) funcionarios.get(id)).clone();
		if(funcionarios.get(id).getClass().equals(b.getClass())) return ((FuncionarioBalcao) funcionarios.get(id)).clone();
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
			FuncionarioBalcao a = new FuncionarioBalcao();
			Tecnico b = new Tecnico();
			Gestor c = new Gestor();
			if(funcionarios.get(id).getPassword().equals(password)) {
				if (funcionarios.get(id).getClass().equals(a.getClass())) return 0;
				if (funcionarios.get(id).getClass().equals(b.getClass())) return 1;
				if (funcionarios.get(id).getClass().equals(c.getClass())) return 2;
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
		List<Tecnico> tecs = new ArrayList<Tecnico>();
		Tecnico a = new Tecnico();
		for(Map.Entry<String, Funcionario> entry: funcionarios.entrySet()){
			if(entry.getValue().getClass().equals(a.getClass())) tecs.add(((Tecnico) entry.getValue()).clone());
		}
		return tecs;
	}

	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		List<FuncionarioBalcao> fb = new ArrayList<FuncionarioBalcao>();
		FuncionarioBalcao a = new FuncionarioBalcao();
		for(Map.Entry<String, Funcionario> entry: funcionarios.entrySet()){
			if(entry.getValue().getClass().equals(a.getClass())) fb.add(((FuncionarioBalcao) entry.getValue()).clone());
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