package SGCRDataLayer.Clientes;

import java.util.*;

public class FichaCliente {

	private String Nome;
	private String NIF;
	private String Email;
	private Set<String> equipamentos;

	public FichaCliente(String nome,String nif, String email){
		this.Nome  = nome;
		this.NIF   = nif;
		this.Email = email;
		this.equipamentos = new HashSet<>();
	}

	public FichaCliente(String nome,String nif, String email, Set<String> equipamentos){
		this.Nome  = nome;
		this.NIF   = nif;
		this.Email = email;
		this.equipamentos = equipamentos != null ? new HashSet<>(equipamentos) : new HashSet<>();
	}

	public FichaCliente(FichaCliente fc){
		this.Nome  = fc.getNome();
		this.NIF   = fc.getNIF();
		this.Email = fc.Email;
		this.equipamentos = new HashSet<>(fc.getEquipamentos());
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
		return new HashSet<>(equipamentos);
	}

	public void setEquipamentos(Set<String> equipamentos) {
		if(equipamentos != null) this.equipamentos = new HashSet<>(equipamentos);
	}

	/**
	 * 
	 * @param idEquip
	 */
	public void addEquip(String idEquip) {
		equipamentos.add(idEquip);
	}

	public FichaCliente clone(){
			return new FichaCliente(Nome,NIF,Email,equipamentos);
	}


}