package SGCRDataLayer.Clientes;

import java.io.Serializable;
import java.util.List;

public interface iClientes extends Serializable {
    /**
     * cria um novo cliente
     * @param Nome string que é o nome do novo cliente
     * @param NIF string que é o identificador/NIF do novo cliente
     * @param Email string que é o email do novo cliente
     * @return um bool dependendo se o cliente foi criado com sucesso ou não
     */
    boolean criaFichaCliente(String Nome, String NIF, String Email);
    /**
     * @param NIF string do identificador do cliente
     * @return FichaCliente associado ao identificador NIF
     */
    FichaCliente getFichaCliente(String NIF);
    /**
     * @return a lista com todos os FichaCliente
     */
    List<FichaCliente> getFichasCLiente();
    /**
     * @param Email string do email associado à FichaCliente em questão
     * @return FichaCLiente associado ao Email, ou null se não existir
     */
    FichaCliente getFichaClientePorEmail(String Email);
    /**
     * adicona um identificador de um equipamento/serviço, à lista de equipamentos na FichaCliente
     * @param idEquip string do identificador do equipamento a ser adicionado
     * @param NIF string do identificador do cliente a quem vai ser adicionado
     */
    boolean addEquipCliente(String idEquip, String NIF);

    /**
     * devolve a string do identificador do próximo equipamento a ser registado e atualiza o mesmo
     * @return string do identificador do próximo equipamento a ser registado
     */
    String getIdProxEquip();
}
