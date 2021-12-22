package SGCRDataLayer.Clientes;

import java.util.*;

public class FichaCliente {

	private String Nome;
	private String NIF;
	private String Email;
	private Set<String> equipamentos;

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getNIF() {
		return NIF;
	}

	public void setNIF(String NIF) {
		this.NIF = NIF;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Set<String> getEquipamentos() {
		return equipamentos;
	}

	public void setEquipamentos(Set<String> equipamentos) {
		this.equipamentos = equipamentos;
	}

	/**
	 * 
	 * @param idEquip
	 */
	public void addEquip(String idEquip) {
		// TODO - implement FichaCliente.addEquip
		throw new UnsupportedOperationException();
	}

}