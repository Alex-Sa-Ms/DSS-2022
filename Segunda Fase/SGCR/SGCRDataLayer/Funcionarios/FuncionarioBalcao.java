package SGCRDataLayer.Funcionarios;

import java.io.Serializable;

public class FuncionarioBalcao extends Funcionario implements Serializable {

	private int nRececoes;
	private int nEntregas;

	/**
	 * Construtor de FuncionarioBalcao
	 * @param id string que é o identificador do funcionario de balcão
	 * @param password string que é a palavra-passe do funcionario de balcão
	 */
	public FuncionarioBalcao(String id, String password) {
		setId(id);
		setPassword(password);
		this.nRececoes = 0;
		this.nEntregas = 0;
	}

	/**
	 * Construtor de FuncionarioBalcao
	 * @param id string que é o identificador do funcionario de balcão
	 * @param password string que é a palavra-passe do funcionario de balcão
	 * @param nRececoes número de pedidos efetuados pelo funcionario de balcão
	 * @param nEntregas número de entregas efetuadas pelo funcionario de balcão
	 */
	public FuncionarioBalcao(String id, String password, int nRececoes, int nEntregas) {
		setId(id);
		setPassword(password);
		this.nRececoes = nRececoes;
		this.nEntregas = nEntregas;
	}
	/**
	 * @return número de pedidos efetuados pelo funcionario de balcão
	 */
	public int getnRececoes() {
		return nRececoes;
	}

	/**
	 * modifica o número de pedidos efetuados pelo funcionario de balcão
	 * @param nRececoes novo número de pedidos efetuados pelo funcionario de balcão
	 */
	public void setnRececoes(int nRececoes) {
		this.nRececoes = nRececoes;
	}

	/**
	 * @return número de entregas efetuadas pelo funcionario de balcão
	 */
	public int getnEntregas() {
		return nEntregas;
	}

	/**
	 * modifica o número de entregas efetuadas pelo funcionario de balcão
	 * @param nEntregas novo número de entregas efetuadas pelo funcionario de balcão
	 */
	public void setnEntregas(int nEntregas) {
		this.nEntregas = nEntregas;
	}

	/**
	 * incrementa o número de pedidos efetuados pelo funcionario de balcão
	 */
	public void incNrRececoes() {
		nRececoes++;
	}

	/**
	 * incrementa o número de entregas efetuadas pelo funcionario de balcão
	 */
	public void incNrEntregas() {
		nEntregas++;
	}

	/**
	 *  @return um FuncionarioBalcao clone daquele que envocou este método
	 */
	public FuncionarioBalcao clone(){
		return new FuncionarioBalcao(this.getId(),this.getPassword(),this.getnRececoes(), this.getnEntregas());
	}
}