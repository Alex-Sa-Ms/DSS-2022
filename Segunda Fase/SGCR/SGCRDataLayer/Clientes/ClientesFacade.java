package SGCRDataLayer.Clientes;

import java.io.Serializable;
import java.util.*;

public class ClientesFacade implements Serializable {

	Map<String,FichaCliente> clientes;
	private int idProxEquipamento;


	public ClientesFacade() {
		this.clientes = new HashMap<>();
		this.idProxEquipamento = 1;
	}

	public ClientesFacade(Map<String, FichaCliente> clientes, int idProxEquipamento) {
		this.clientes = clientes;
		this.idProxEquipamento = idProxEquipamento;
	}

	/**
	 * 
	 * @param Nome
	 * @param NIF
	 * @param Email
	 */
	public boolean criaFichaCliente(String Nome, String NIF, String Email) {

		clientes.put(NIF, new FichaCliente(Nome, NIF, Email));

		return clientes.containsKey(NIF);
	}

	/**
	 * 
	 * @param NIF
	 */
	public FichaCliente getFichaCliente(String NIF) {
		FichaCliente c =  clientes.get(NIF);
		if (c != null) return c.clone();
		else return null;
	}

	/**
	 * 
	 * @param Email
	 */
	public FichaCliente getFichaClientePorEmail(String Email) {

		for (Map.Entry<String,FichaCliente> entry : clientes.entrySet()){
			if(entry.getValue().getEmail().equals(Email)) return entry.getValue().clone();
		}
		return null;
	}

	/**
	 * 
	 * @param idEquip
	 * @param NIF
	 */
	public boolean addEquipCliente(String idEquip, String NIF) {
		if(!clientes.containsKey(NIF)) return false;
		clientes.get(NIF).addEquip(idEquip);
		return true;
	}

	public String getIdProxEquip() {
		String id = String.valueOf(idProxEquipamento);
		idProxEquipamento++;
		return id;
	}
}