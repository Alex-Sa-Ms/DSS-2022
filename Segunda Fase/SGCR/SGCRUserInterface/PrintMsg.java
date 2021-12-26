package SGCRUserInterface;

import SGCRDataLayer.Clientes.FichaCliente;
import SGCRDataLayer.Funcionarios.Funcionario;
import SGCRDataLayer.Funcionarios.FuncionarioBalcao;
import SGCRDataLayer.Funcionarios.Tecnico;
import SGCRDataLayer.PedidosDeOrcamento.PedidoOrcamento;
import SGCRDataLayer.Servicos.Passo;
import SGCRDataLayer.Servicos.Servico;
import SGCRDataLayer.Servicos.ServicoExpresso;
import SGCRDataLayer.Servicos.ServicoPadrao;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class PrintMsg {

    public void printMsg (String msg){
        System.out.println(msg);
        System.out.flush();
    }

    public void printServico(Servico s){
        if (s instanceof ServicoPadrao){
            System.out.print(servicoToString(s));

        }
        else if (s instanceof ServicoExpresso){
            System.out.print(servicoToString(s));
        }
        System.out.flush();
    }

    public String servicoToString(Servico s) {
        StringBuilder sb = new StringBuilder();
        if (s instanceof ServicoPadrao) {

            sb.append("Servi�o Padr�o #").append(s.getId()).append("::: Estado: ").append(s.getEstado()).append(" | Custo: ").append(((ServicoPadrao) s).getCusto()).append("\n");
        } else if (s instanceof ServicoExpresso) {

            sb.append("Servi�o Expresso #").append(s.getId()).append("::: Estado: ").append(s.getEstado()).append(" | Custo: ").append(((ServicoExpresso) s).getCusto()).append("\n");
        }
        return sb.toString();

    }
    public void printPedido (PedidoOrcamento p){
        System.out.printf("Pedido de Or�amento::: Equipamento: %s | NIF : %s | Descri��o: %s \n",p.getIdEquipamento(),p.getNIFCliente(),p.getDescricao());
        System.out.flush();
    }

    public void printLPasso (List<Passo> l){
        int counter=1;
        for(Passo p : l){
            System.out.println("Passo #"+counter+ ":::: Custo : "+  p.getCusto() + " | Tempo: "+p.getTempo()+ " | Descri��o: "+ p.getDescricao() );
            System.out.flush();
            counter++;
        }
    }

    public String passoToString(Passo p){
        String s = "Passo :::: Custo : "+ p.getCusto() + " | Tempo: "+p.getTempo()+ " | Descri��o: "+ p.getDescricao();
        return s;
    }

    public void printFuncionario(Funcionario f){
        if (f instanceof FuncionarioBalcao){
            System.out.println("Funcionario Balcao: " + f.getId());
        }
        else if  (f instanceof Tecnico){
            System.out.println("Tecnico: " + f.getId());
        }
    }

    public void printLFichaCliente(List<FichaCliente> c){
        for (FichaCliente x: c){
            System.out.println("Cliente ::: NIF: "+ x.getNIF()+ " | Nome: "+ x.getNome() + " | Email: " + x.getEmail());
            System.out.flush();
        }
    }

    private String Nome;
    private String NIF;
    private String Email;
    private Set<String> equipamentos;
    /*
    public static void clrscr(){

        //Clears Screen in java

        try {

            if (System.getProperty("os.name").contains("Windows"))

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else

                Runtime.getRuntime().exec("clear");

        } catch (InterruptedException | IOException ex) {}

    }
    */



}
