package SGCRDataLayer.Funcionarios;

import java.io.Serializable;

public class Gestor extends Funcionario implements Serializable {

    /**
     * Construtor de Gestor
     * @param id string que é o identificador do gestor
     * @param password string que é a palavra-passe do gestor
     */
    public Gestor(String id, String password) {
        setId(id);
        setPassword(password);
    }

    /**
     *  @return um Gestor clone daquele que envocou este método
     */
    public Gestor clone(){
        return new Gestor(this.getId(),this.getPassword());
    }
}