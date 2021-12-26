package SGCRDataLayer.Funcionarios;

import java.io.Serializable;

public class Gestor extends Funcionario implements Serializable {

    public Gestor(String id, String password) {
        setId(id);
        setPassword(password);
    }

    public Gestor clone(){
        return new Gestor(this.getId(),this.getPassword());
    }
}