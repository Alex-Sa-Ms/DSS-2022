package SGCRUserInterface;

import java.util.Scanner;

public class MenuInput {
    private String title;
    private String text;
    private String op;


    public MenuInput(String title,String text){
        this.title = title;
        this.text =text;
        this.op = "";
    }

    public void executa() {
        showMenu();
        this.op = lerOpcao();
    }


    private void showMenu() {
        if (title != null)  System.out.println(title);
        //System.out.println(text);
    }

    private String lerOpcao() {
        String op;
        Scanner is = new Scanner(System.in);

        op = is.nextLine();
        System.out.println("");

        return op;
    }
    public String getOpcao() {
        return this.op;
    }

}
