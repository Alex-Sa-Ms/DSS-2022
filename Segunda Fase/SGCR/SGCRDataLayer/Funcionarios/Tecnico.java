package SGCRDataLayer.Funcionarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tecnico extends Funcionario implements Serializable {
	private HashSet<String> servicos;
	private int nRepProgramadasConcluidas;
	private int nRepExpressoConcluidas;
	private float duracaoMediaRepProg;
	private float mediaDesvioRepProg;

	/**
	 * Construtor de Tecnico
	 * @param id string que é o identificador do tecnico
	 * @param password string que é a palavra-passe do tecnico
	 */
	public Tecnico(String id, String password) {
		setId(id);
		setPassword(password);
		servicos = new HashSet<>();
		nRepExpressoConcluidas    = 0;
		nRepProgramadasConcluidas = 0;
		mediaDesvioRepProg  = 0;
		duracaoMediaRepProg = 0;
	}

	/**
	 * Construtor de Tecnico
	 * @param id string que é o identificador do tecnico
	 * @param password string que é a palavra-passe do tecnico
	 * @param servicos lista de identificadores de serviços completados pelos técnico
	 * @param nRepProgramadasConcluidas número de reparações padrão concluidas pelo técnico
	 * @param nRepExpressoConcluidas número de reparações expresso concluidas pelo técnico
	 * @param duracaoMediaRepProg duração média de reparações padrão concluidas pelo técnico
	 * @param mediaDesvioRepProg desvio da duração média perante á duração prevista das reparações concluidas pelo técnico
	 */
	public Tecnico(
			String id,
			String password,
			HashSet<String> servicos,
			int nRepProgramadasConcluidas,
			int nRepExpressoConcluidas,
			float duracaoMediaRepProg,
			float mediaDesvioRepProg) {
		setId(id);
		setPassword(password);
		this.servicos = servicos != null ? new HashSet<>(servicos) : new HashSet<>();
		this.nRepProgramadasConcluidas = nRepProgramadasConcluidas;
		this.nRepExpressoConcluidas = nRepExpressoConcluidas;
		this.duracaoMediaRepProg = duracaoMediaRepProg;
		this.mediaDesvioRepProg = mediaDesvioRepProg;
	}


	/**
	 * adicona um identificador de serviço à lista de identificadores de serviços completados pelos técnico
	 * @param id identificador do serviço completado pelo técnico
	 */
	public boolean addServico(String id) {
		return servicos.add(id);
	}

	/**
	 * @return lista de identificadores de serviços completados pelos técnico
	 */
	public List<String> getServicos() {
		return new ArrayList<>(servicos);
	}

	/**
	 * modifica a lista de identificadores de serviços completados pelos técnico
	 * @param servicos nova lista de identificadores de serviços completados pelos técnico
	 */
	protected void setServicos(HashSet<String> servicos) {
		if(servicos != null) this.servicos = new HashSet<>(servicos);
	}

	/**
	 * @return número de reparações padrão concluidas pelo técnico
	 */
	public int getnRepProgramadasConcluidas() {
		return nRepProgramadasConcluidas;
	}

	/**
	 * modifica o número de reparações padrão concluidas pelo técnico
	 * @param nRepProgramadasConcluidas número de reparações padrão concluidas pelo técnico
	 */
	protected void setnRepProgramadasConcluidas(int nRepProgramadasConcluidas) {
		this.nRepProgramadasConcluidas = nRepProgramadasConcluidas;
	}

	/**
	 * @return número de reparações expresso concluidas pelo técnico
	 */
	public int getnRepExpressoConcluidas() {
		return nRepExpressoConcluidas;
	}

	/**
	 * modifica o número de reparações expresso concluidas pelo técnico
	 * @param nRepExpressoConcluidas número de reparações expresso concluidas pelo técnico
	 */
	protected void setnRepExpressoConcluidas(int nRepExpressoConcluidas) {
		this.nRepExpressoConcluidas = nRepExpressoConcluidas;
	}

	/**
	 * @return duração média de reparações padrão concluidas pelo técnico
	 */
	public float getDuracaoMediaRepProg() {
		return duracaoMediaRepProg;
	}

	/**
	 * modifica a duração média de reparações padrão concluidas pelo técnico
	 * @param duracaoMediaRepProg nova duração média de reparações padrão concluidas pelo técnico
	 */
	protected void setDuracaoMediaRepProg(float duracaoMediaRepProg) {
		this.duracaoMediaRepProg = duracaoMediaRepProg;
	}

	/**
	 * @return desvio da duração média perante á duração prevista das reparações concluidas pelo técnico
	 */
	public float getMediaDesvioRepProg() {
		return mediaDesvioRepProg;
	}

	/**
	 * modifica o desvio da duração média perante á duração prevista das reparações concluidas pelo técnico
	 * @param mediaDesvioRepProg desvio da duração média perante á duração prevista das reparações concluidas pelo técnico
	 */
	protected void setMediaDesvioRepProg(float mediaDesvioRepProg) {
		this.mediaDesvioRepProg = mediaDesvioRepProg;
	}

	/**
	 * incrementa o número de reparações padrão concluidas pelo técnico e atualiza a duração média de reparações padrão
	 * e o desvio da duração média perante á duração prevista da reparação concluidas pelo técnico
	 * @param duracao duração média da reparação padrão concluidas pelo técnico a adicionar
	 * @param desvio desvio da duração média perante á duração prevista da reparação concluidas pelo técnico
	 */
	public void incNrRepProgConcluidas(float duracao, float desvio) {
		duracaoMediaRepProg = atualizaMedia(nRepProgramadasConcluidas, duracaoMediaRepProg, duracao);
		mediaDesvioRepProg  = atualizaMedia(nRepProgramadasConcluidas, mediaDesvioRepProg, desvio);
		nRepProgramadasConcluidas++;
	}

	/**
	 * incrementa o número de reparações expresso concluidas pelo técnico
	 */
	public void incNrRepExpConcluidas() {
		nRepExpressoConcluidas++;
	}

	/**
	 * verifica se o tecnico possui um serviço na lista
	 * @param idServico string do identificador do serviço a ser verificado
	 * @return true se possuir e false se não possuir
	 */
	public boolean possuiServico(String idServico){
		return servicos.contains(idServico);
	}

	/**
	 *  @return um Tecnico clone daquele que envocou este método
	 */
	public Tecnico clone(){
		return new Tecnico(
				this.getId(),
				this.getPassword(),
				this.servicos,
				this.getnRepProgramadasConcluidas(),
				this.getnRepExpressoConcluidas(),
				this.getDuracaoMediaRepProg(),
				this.getMediaDesvioRepProg());
	}


	// ****** Auxiliares ******

	/**
	 * @param nrValores número de valores que constituem a média atual
	 * @param media média de valores
	 * @param novoValor novo valor, do qual se pretende a participacao na media
	 * @return média atualizada
	 */
	private static float atualizaMedia(int nrValores, float media, float novoValor){
		if(nrValores == 0) return novoValor;
		else return ((nrValores * media) + novoValor) / (nrValores + 1);
	}
}