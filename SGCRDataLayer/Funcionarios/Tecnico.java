package SGCRDataLayer.Funcionarios;

public class Tecnico extends Funcionario {

	private HashSet<java.lang.String> servicos;
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