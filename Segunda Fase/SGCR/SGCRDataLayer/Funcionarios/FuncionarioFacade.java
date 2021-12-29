package SGCRDataLayer.Funcionarios;

import java.io.Serializable;
import java.util.*;

public class FuncionarioFacade implements Serializable {

	Map<String,Funcionario> funcionarios;

	/**
	 * Construtor de FuncionarioFacade
	 */
	public FuncionarioFacade() {
		funcionarios = new HashMap<>();
		funcionarios.put("Alfredo",new Gestor("Alfredo","12345"));
	}

	/**
	 * Construtor de Tecnico
	 * @param funcionarios mapa que asssocia o identificador do funcionário(chave) ao respetivo Funcionario(valor)
	 */
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
	 * @param id string que é o identificador do funcionário que se está à procura
	 * @return Funcionario que se está à procura
	 */
	public Funcionario getFuncionario(String id) {
		Funcionario funcionario = funcionarios.get(id);
		if(funcionario != null) return funcionario.clone();
		return null;
	}

	/**
	 * verifica se existe um funcionário com tais credenciais
	 * @param id string que é o identificador do funcionário que está a ser verificado
	 * @param password string que é a palavra-passe do funcionário que está a ser verificado
	 * @return
	 * 			-1 login incorreto
	 * 			0  funcionario balcao
	 * 			1  tecnico
	 * 			2  gestor
	 */
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
	 * aumentar o número de receções de um determinado funcionário de balcão
	 * @param idFuncBalcao string que é identificador do funcionário de balcão a quem se está a incrementar o número de receções
	 * @return false
	 */
	public boolean incNrRececoes(String idFuncBalcao) {
		Funcionario funcionario = funcionarios.get(idFuncBalcao);
		if(funcionario instanceof FuncionarioBalcao)
			((FuncionarioBalcao) funcionario).incNrRececoes();
		return false;
	}

	/**
	 * aumentar o número de entregas de um determinado funcionário de balcão
	 * @param idFuncBalcao string que é identificador do funcionário de balcão a quem se está a incrementar o número de entregas
	 * @return false
	 */
	public boolean incNrEntregas(String idFuncBalcao) {
		Funcionario funcionario = funcionarios.get(idFuncBalcao);
		if(funcionario instanceof FuncionarioBalcao)
			((FuncionarioBalcao) funcionario).incNrEntregas();
		return false;
	}

	/**
	 * adicionar o identificador de um serviço a um determinado tecnico
	 * @param idTecnico string que é o identificador do tecnico a quem se está a adicionar o identificador de um serviço
	 * @param idServico tring que é o identificador do serviço que está a ser adicionado
	 */
	public boolean addServicoTecnico(String idTecnico, String idServico) {
		if(funcionarios.containsKey(idServico)) return false;
		return ((Tecnico) funcionarios.get(idTecnico)).addServico(idServico);
	}

	/**
	 * incrementa o número de reparações padrão concluidas por um determinado técnico e atualiza a duração média de reparações padrão
	 * @param idTecnico string que é o identificador do tecnico a quem se está a incrementar o reparações padrão concluidas
	 * @param duracao duração média da reparação padrão
	 * @param duracaoPrevista duração prevista da reparação padrão
	 */
	public void incNrRepProgConcluidas(String idTecnico, float duracao, float duracaoPrevista) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			((Tecnico) funcionario).incNrRepProgConcluidas(duracao,duracaoPrevista);
	}

	/**
	 * incrementa o número de reparações expresso concluidas pelo técnico
	 * @param idTecnico string que é o identificador do tecnico a quem se está a incrementar o reparações expresso concluidas
	 */
	public void incNrRepExpConcluidas(String idTecnico) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			((Tecnico) funcionario).incNrRepExpConcluidas();
	}

	/**
	 * @return lista de todos os funcionários que são técnicos
	 */
	public List<Tecnico> listarTecnicos() {
		List<Tecnico> tecs = new ArrayList<>();

		for(Funcionario func: funcionarios.values())
			if(func instanceof Tecnico)
				tecs.add(((Tecnico) func).clone());

		return tecs;
	}

	/**
	 * @return lista de todos os funcionários que são funcionários de balcão
	 */
	public List<FuncionarioBalcao> listarFuncionariosBalcao() {
		List<FuncionarioBalcao> fb = new ArrayList<>();

		for(Funcionario func: funcionarios.values())
			if(func instanceof FuncionarioBalcao)
				fb.add(((FuncionarioBalcao) func).clone());

		return fb;
	}

	/**
	 * listar os serviços de um determinado técnico
	 * @param idTecnico string que é o identificador do tecnico
	 * @return lista de identificadores de serviço
	 */
	public List<String> listarServicosTecnico(String idTecnico) {
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico)
			return ((Tecnico) funcionario).getServicos();
		return null;
	}

	/**
	 * verifica se um determinado técnico possui um determinado serviço
	 * @param idTecnico string que é o identificador do tecnico
	 * @param idServico string que é o identificador do serviço
	 * @return true se possuir e false se não possuir
	 */
	public boolean possuiServico(String idTecnico, String idServico){
		Funcionario funcionario = funcionarios.get(idTecnico);
		if(funcionario instanceof Tecnico) return ((Tecnico) funcionario).possuiServico(idServico);
		return false;
	}
}