package SGCRUserInterface;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuSelect {


        private String title;
        private List<String> options;
        private int op;


        public MenuSelect(String title, String[] opcoes){
            this.title = title;
            this.options = Arrays.asList(opcoes);
            this.op = -1;
        }

        public void executa() {
            do {
                showMenu();
                this.op = lerOpcao();
            } while (this.op == -1);
        }


        private void showMenu() {
            if (title!= null) System.out.print("\n"+this.title + "\n");
            for (int i=0; i<this.options.size(); i++) {
                System.out.print(i+1);
                System.out.print(" - ");
                System.out.println(this.options.get(i));
            }
            System.out.println("0 - Sair");
        }

        private int lerOpcao() {
            int op;
            Scanner is = new Scanner(System.in);

            System.out.print("Opcao: ");
            try {
                op = is.nextInt();
            }
            catch (InputMismatchException e) {
                op = -1;
            }
            if (op<0 || op>this.options.size()) {
                System.out.println("Opção Inválida!!!");
                op = -1;
            }
            return op;
        }
        public int getOpcao() {
            return this.op;
        }
    }

