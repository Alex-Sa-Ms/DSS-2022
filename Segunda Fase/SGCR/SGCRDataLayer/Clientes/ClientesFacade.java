package SGCRDataLayer.Clientes;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ClientesFacade implements Serializable,iClientes {

	Map<String,FichaCliente> clientes;
	private int idProxEquipamento;

	/**
	 * Construtor do ClientesFacade
	 */
	public ClientesFacade() {
		this.clientes = new HashMap<>();
		this.idProxEquipamento = 1;
	}

	/**
	 * cria um novo cliente
	 * @param Nome string que é o nome do novo cliente
	 * @param NIF string que é o identificador/NIF do novo cliente
	 * @param Email string que é o email do novo cliente
	 * @return um bool dependendo se o cliente foi criado com sucesso ou não
	 */
	public boolean criaFichaCliente(String Nome, String NIF, String Email) {

		clientes.put(NIF, new FichaCliente(Nome, NIF, Email));

		return clientes.containsKey(NIF);
	}

	/**
	 * @param NIF string do identificador do cliente
	 * @return FichaCliente associado ao identificador NIF
	 */
	public FichaCliente getFichaCliente(String NIF) {
		FichaCliente c =  clientes.get(NIF);
		if (c != null) return c.clone();
		else return null;
	}

	/**
	 * @return a lista com todos os FichaCliente
	 */
	public List<FichaCliente> getFichasCLiente(){
		return clientes.values().stream().map(FichaCliente::clone).collect(Collectors.toList());
	}

	/**
	 * @param Email string do email associado à FichaCliente em questão
	 * @return FichaCLiente associado ao Email, ou null se não existir
	 */
	public FichaCliente getFichaClientePorEmail(String Email) {

		for (Map.Entry<String,FichaCliente> entry : clientes.entrySet()){
			if(entry.getValue().getEmail().equals(Email)) return entry.getValue().clone();
		}
		return null;
	}

	/**
	 * adicona um identificador de um equipamento/serviço, à lista de equipamentos na FichaCliente
	 * @param idEquip string do identificador do equipamento a ser adicionado
	 * @param NIF string do identificador do cliente a quem vai ser adicionado
	 */
	public boolean addEquipCliente(String idEquip, String NIF) {
		if(!clientes.containsKey(NIF)) return false;
		clientes.get(NIF).addEquip(idEquip);
		return true;
	}

	/**
	 * devolve a string do identificador do próximo equipamento a ser registado e atualiza o mesmo
	 * @return string do identificador do próximo equipamento a ser registado
	 */
	public String getIdProxEquip() {
		String id = String.valueOf(idProxEquipamento);
		idProxEquipamento++;
		return id;
	}
}