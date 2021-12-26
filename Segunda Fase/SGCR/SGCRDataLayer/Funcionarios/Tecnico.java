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


	public Tecnico(String id, String password) {
		setId(id);
		setPassword(password);
		servicos = new HashSet<>();
		nRepExpressoConcluidas    = 0;
		nRepProgramadasConcluidas = 0;
		mediaDesvioRepProg  = 0;
		duracaoMediaRepProg = 0;
	}

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
	 * 
	 * @param id
	 */
	public boolean addServico(String id) {
		return servicos.add(id);
	}


	public List<String> getServicos() {
		return new ArrayList<>(servicos);
	}

	protected void setServicos(HashSet<String> servicos) {
		if(servicos != null) this.servicos = new HashSet<>(servicos);
	}

	public int getnRepProgramadasConcluidas() {
		return nRepProgramadasConcluidas;
	}

	protected void setnRepProgramadasConcluidas(int nRepProgramadasConcluidas) {
		this.nRepProgramadasConcluidas = nRepProgramadasConcluidas;
	}

	public int getnRepExpressoConcluidas() {
		return nRepExpressoConcluidas;
	}

	protected void setnRepExpressoConcluidas(int nRepExpressoConcluidas) {
		this.nRepExpressoConcluidas = nRepExpressoConcluidas;
	}

	public float getDuracaoMediaRepProg() {
		return duracaoMediaRepProg;
	}

	protected void setDuracaoMediaRepProg(float duracaoMediaRepProg) {
		this.duracaoMediaRepProg = duracaoMediaRepProg;
	}

	public float getMediaDesvioRepProg() {
		return mediaDesvioRepProg;
	}

	protected void setMediaDesvioRepProg(float mediaDesvioRepProg) {
		this.mediaDesvioRepProg = mediaDesvioRepProg;
	}

	/**
	 * 
	 * @param duracao
	 * @param desvio
	 */
	public void incNrRepProgConcluidas(float duracao, float desvio) {
		duracaoMediaRepProg = atualizaMedia(nRepProgramadasConcluidas, duracaoMediaRepProg, duracao);
		mediaDesvioRepProg  = atualizaMedia(nRepProgramadasConcluidas, mediaDesvioRepProg, desvio);
		nRepProgramadasConcluidas++;
	}

	public void incNrRepExpConcluidas() {
		nRepExpressoConcluidas++;
	}

	public boolean possuiServico(String idServico){
		return servicos.contains(idServico);
	}

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