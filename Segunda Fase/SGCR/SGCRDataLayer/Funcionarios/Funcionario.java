package SGCRDataLayer.Funcionarios;

import java.util.HashSet;

public abstract class Funcionario {

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


}