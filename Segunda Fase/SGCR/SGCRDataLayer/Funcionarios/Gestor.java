package SGCRDataLayer.Funcionarios;

public class Gestor extends Funcionario {

    public Gestor(String id, String password) {
        setId(id);
        setPassword(password);
    }

    public Gestor clone(){
        return new Gestor(this.getId(),this.getPassword());
    }
}