package SGCRDataLayer.Funcionarios;

import java.io.Serializable;

public abstract class Funcionario implements Serializable {

	private String id;
	private String password;

	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	public abstract Funcionario clone();
}