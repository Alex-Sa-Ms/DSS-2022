package SGCRDataLayer.Clientes;

import java.io.Serializable;
import java.util.*;

public class FichaCliente implements Serializable {

	private String Nome;
	private String NIF;
	private String Email;
	private Set<String> equipamentos;

	/**
	 * Construtor de FichaCliente
	 * @param nome string que é o nome do cliente
	 * @param nif string que é o identificador/NIF do cliente
	 * @param email string que é o email do cliente
	 */
	public FichaCliente(String nome,String nif, String email){
		this.Nome  = nome;
		this.NIF   = nif;
		this.Email = email;
		this.equipamentos = new HashSet<>();
	}

	/**
	 * Construtor de FichaCliente
	 * @param nome string que é o nome do cliente
	 * @param nif string que é o identificador/NIF do cliente
	 * @param email string que é o email do cliente
	 * @param equipamentos lista de equipamentos/serviços associados ao cliente
	 */
	public FichaCliente(String nome,String nif, String email, Set<String> equipamentos){
		this.Nome  = nome;
		this.NIF   = nif;
		this.Email = email;
		this.equipamentos = equipamentos != null ? new HashSet<>(equipamentos) : new HashSet<>();
	}

	/**
	 * Construtor de FichaCliente
	 * @param fc FichaCliente do cliente
	 */
	public FichaCliente(FichaCliente fc){
		this.Nome  = fc.getNome();
		this.NIF   = fc.getNIF();
		this.Email = fc.Email;
		this.equipamentos = new HashSet<>(fc.getEquipamentos());
	}

	/**
	 * @return string do nome do cliente
	 */
	public String getNome() {
		return Nome;
	}

	/**
	 * modifica o nome do cliente
	 * @param nome string que é o novo nome do cliente
	 */
	public void setNome(String nome) {
		Nome = nome;
	}

	/**
	 * @return string do identificador/NIF do cliente
	 */
	public String getNIF() {
		return NIF;
	}

	/**
	 * modifica o identificador/NIF do cliente
	 * @param NIF string que é o novo identificador/NIF  do cliente
	 */
	public void setNIF(String NIF) {
		this.NIF = NIF;
	}

	/**
	 * @return string do email do cliente
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * modifica o email do cliente
	 * @param email string que é o novo email do cliente
	 */
	public void setEmail(String email) {
		Email = email;
	}

	/**
	 * @return string da lista de equipamentos/serviços associados ao cliente
	 */
	public Set<String> getEquipamentos() {
		return new HashSet<>(equipamentos);
	}
	/**
	 * modifica a lista de equipamentos associados ao cliente
	 * @param equipamentos string que é a nova lista de equipamentos associados ao cliente
	 */
	public void setEquipamentos(Set<String> equipamentos) {
		if(equipamentos != null) this.equipamentos = new HashSet<>(equipamentos);
	}

	/**
	 * adiciona um equipamento/serviço à lista de eqipamentos associados ao cliente
	 * @param idEquip string do identificador do equipamento a ser adicionado
	 */
	public void addEquip(String idEquip) {
		equipamentos.add(idEquip);
	}

	/**
	 * @return uma FichaCliente clone daquela que envocou este método
	 */
	public FichaCliente clone(){
			return new FichaCliente(Nome,NIF,Email,equipamentos);
	}


}