package SGCRDataLayer.Clientes;

import java.util.*;

public class FichaCliente {

	private String Nome;
	private String NIF;
	private String Email;
	private Set<String> equipamentos;

	public FichaCliente(String nome,String nif, String email){
		this.Nome = nome;
		this.NIF = nif;
		this.Email = email;
		this.equipamentos = new HashSet<String>();
	}

	public FichaCliente(String nome,String nif, String email, Set<String> equipamentos){
		this.Nome = nome;
		this.NIF = nif;
		this.Email = email;
		this.equipamentos = new HashSet<String>(equipamentos);
	}

	public FichaCliente(FichaCliente fc){
		this.Nome = fc.getNome();
		this.NIF = fc.getNIF();
		this.Email = fc.Email;
		this.equipamentos = new HashSet<String>(fc.getEquipamentos());
	}

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
	public boolean addEquip(String idEquip) {

		return equipamentos.add(idEquip);
	}

	public FichaCliente clone(){

			return new FichaCliente(this.getNome(),this.getNIF(),this.getEmail(),this.getEquipamentos());

	}


}