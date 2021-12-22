package SGCRDataLayer.Funcionarios;

import java.util.HashSet;

public class Tecnico extends Funcionario {
	private HashSet<String> servicos;
	private int nRepProgramadasConcluidas;
	private int nRepExpressoConcluidas;
	private float duracaoMediaRepProg;
	private float duracaoMediaPrevistaRepProg;


	public Tecnico(String id, String password) {
		setId(id);
		setPassword(password);
		servicos = new HashSet<>();
		nRepExpressoConcluidas = 0;
		nRepProgramadasConcluidas = 0;
		duracaoMediaPrevistaRepProg = 0;
		duracaoMediaRepProg = 0;
	}

	public Tecnico(
			String id,
			String password,
			HashSet<String> servicos,
			int nRepProgramadasConcluidas,
			int nRepExpressoConcluidas,
			float duracaoMediaRepProg,
			float duracaoMediaPrevistaRepProg) {
		setId(id);
		setPassword(password);
		this.servicos = servicos;
		this.nRepProgramadasConcluidas = nRepProgramadasConcluidas;
		this.nRepExpressoConcluidas = nRepExpressoConcluidas;
		this.duracaoMediaRepProg = duracaoMediaRepProg;
		this.duracaoMediaPrevistaRepProg = duracaoMediaPrevistaRepProg;
	}


	/**
	 * 
	 * @param id
	 */
	public boolean addServico(String id) {
		return servicos.add(id);
	}


	public HashSet<String> getServicos() {
		return servicos;
	}

	protected void setServicos(HashSet<String> servicos) {
		this.servicos = servicos;
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

	public float getDuracaoMediaPrevistaRepProg() {
		return duracaoMediaPrevistaRepProg;
	}

	protected void setDuracaoMediaPrevistaRepProg(float duracaoMediaPrevistaRepProg) {
		this.duracaoMediaPrevistaRepProg = duracaoMediaPrevistaRepProg;
	}

	/**
	 * 
	 * @param duracao
	 * @param duracaoPrevista
	 */
	public void incNrRepProgConcluidas(float duracao, float duracaoPrevista) {
		duracaoMediaRepProg         += duracao;
		duracaoMediaPrevistaRepProg += duracaoPrevista;
		nRepProgramadasConcluidas++;
	}

	public void incNrRepExpConcluidas() {
		nRepExpressoConcluidas++;
	}

	public Tecnico clone(){
		return new Tecnico(
				this.getId(),
				this.getPassword(),
				this.getServicos(),
				this.getnRepProgramadasConcluidas(),
				this.getnRepExpressoConcluidas(),
				this.getDuracaoMediaRepProg(),
				this.getDuracaoMediaPrevistaRepProg());
	}

}