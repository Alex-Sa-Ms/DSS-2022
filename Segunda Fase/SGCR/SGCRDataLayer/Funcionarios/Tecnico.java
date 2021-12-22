package SGCRDataLayer.Funcionarios;

import java.util.HashSet;

public class Tecnico extends Funcionario {

	private HashSet<String> servicos;
	private int nRepProgramadasConcluidas;
	private int nRepExpressoConcluidas;
	private float duracaoMediaRepProg;
	private float duracaoMediaPrevistaRepProg;

	/**
	 * 
	 * @param id
	 */
	public boolean addServico(String id) {
		// TODO - implement Tecnico.addServico
		throw new UnsupportedOperationException();
	}

	public HashSet<String> getServicos() {
		return servicos;
	}

	public void setServicos(HashSet<String> servicos) {
		this.servicos = servicos;
	}

	public int getnRepProgramadasConcluidas() {
		return nRepProgramadasConcluidas;
	}

	public void setnRepProgramadasConcluidas(int nRepProgramadasConcluidas) {
		this.nRepProgramadasConcluidas = nRepProgramadasConcluidas;
	}

	public int getnRepExpressoConcluidas() {
		return nRepExpressoConcluidas;
	}

	public void setnRepExpressoConcluidas(int nRepExpressoConcluidas) {
		this.nRepExpressoConcluidas = nRepExpressoConcluidas;
	}

	public float getDuracaoMediaRepProg() {
		return duracaoMediaRepProg;
	}

	public void setDuracaoMediaRepProg(float duracaoMediaRepProg) {
		this.duracaoMediaRepProg = duracaoMediaRepProg;
	}

	public float getDuracaoMediaPrevistaRepProg() {
		return duracaoMediaPrevistaRepProg;
	}

	public void setDuracaoMediaPrevistaRepProg(float duracaoMediaPrevistaRepProg) {
		this.duracaoMediaPrevistaRepProg = duracaoMediaPrevistaRepProg;
	}

	/**
	 * 
	 * @param duracao
	 * @param duracaoPrevista
	 */
	public void incNrRepProgConcluidas(float duracao, float duracaoPrevista) {
		// TODO - implement Tecnico.incNrRepProgConcluidas
		throw new UnsupportedOperationException();
	}

	public void incNrRepExpConcluidas() {
		// TODO - implement Tecnico.incNrRepExpConcluidas
		throw new UnsupportedOperationException();
	}

}