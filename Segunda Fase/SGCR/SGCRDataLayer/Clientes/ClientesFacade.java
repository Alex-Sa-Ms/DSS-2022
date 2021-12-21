package SGCRDataLayer.Clientes;

import java.util.Map;

public class ClientesFacade {

	Map<String,FichaCliente> clientes;
	private int idProxEquipamento;

	/**
	 * 
	 * @param Nome
	 * @param NIF
	 * @param Email
	 */
	public boolean criaFichaCliente(String Nome, String NIF, String Email) {
		// TODO - implement ClientesFacade.criaFichaCliente
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param NIF
	 */
	public FichaCliente getFichaCliente(String NIF) {
		// TODO - implement ClientesFacade.getFichaCliente
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Email
	 */
	public FichaCliente getFichaClientePorEmail(String Email) {
		// TODO - implement ClientesFacade.getFichaClientePorEmail
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idEquip
	 * @param NIF
	 */
	public boolean addEquipCliente(String idEquip, FichaCliente NIF) {
		// TODO - implement ClientesFacade.addEquipCliente
		throw new UnsupportedOperationException();
	}

	public int getIdProxEquip() {
		// TODO - implement ClientesFacade.getIdProxEquip
		throw new UnsupportedOperationException();
	}

}