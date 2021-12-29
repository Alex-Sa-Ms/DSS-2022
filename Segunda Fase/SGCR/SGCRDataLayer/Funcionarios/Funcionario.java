package SGCRDataLayer.Funcionarios;

import java.io.Serializable;

public abstract class Funcionario implements Serializable {

	private String id;
	private String password;

	/**
	 * @return string que é o identificador do funcionário
	 */
	public String getId() {
		return id;
	}

	/**
	 * modifica o identificador do funcionário
	 * @param id string que é o novo identificador do funcionário
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * @return string que é a palavra-passe do funcionário
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * modifica a palavra-passe do funcionário
	 * @param password string que é a nova palavra-passe do funcionário
	 */
	protected void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return um Funcionario clone daquele que envocou este método
	 */
	public abstract Funcionario clone();
}