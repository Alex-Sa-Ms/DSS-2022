package SGCRDataLayer.Funcionarios;

public class FuncionarioBalcao extends Funcionario {

	private int nRececoes;
	private int nEntregas;

	public FuncionarioBalcao(String id, String password) {
		setId(id);
		setPassword(password);
		this.nRececoes = 0;
		this.nEntregas = 0;
	}

	public FuncionarioBalcao(String id, String password, int nRececoes, int nEntregas) {
		setId(id);
		setPassword(password);
		this.nRececoes = nRececoes;
		this.nEntregas = nEntregas;
	}

	public int getnRececoes() {
		return nRececoes;
	}

	public void setnRececoes(int nRececoes) {
		this.nRececoes = nRececoes;
	}

	public int getnEntregas() {
		return nEntregas;
	}

	public void setnEntregas(int nEntregas) {
		this.nEntregas = nEntregas;
	}

	public void incNrRececoes() {
		nRececoes++;
	}

	public void incNrEntregas() {
		nEntregas++;
	}

	public FuncionarioBalcao clone(){
		return new FuncionarioBalcao(this.getId(),this.getPassword(),this.getnRececoes(), this.getnEntregas());
	}
}