package SGCRUserInterface;
import SGCRDataLayer.PedidosDeOrcamento.PedidoOrcamento;
import SGCRDataLayer.Servicos.Passo;
import SGCRDataLayer.Servicos.Servico;
import SGCRDataLayer.Servicos.ServicoExpresso;
import SGCRDataLayer.Servicos.ServicoPadrao;
import SGCRLogicLayer.SGCRFacade;
import SGCRLogicLayer.iSGCR;
import java.util.ArrayList;
import java.util.List;


public class UIFacade {
    PrintMsg printer = new PrintMsg();
    iSGCR logic= new SGCRFacade();



    public int login(){
        printer.printMsg("Bem vindo a Sistema de Gestao do Centro de Reparacoes!");
        MenuInput username = new MenuInput("Por favor insira o seu Username","Username:");
        MenuInput password = new MenuInput("Por favor insira a sua palavra passe", "Password:");
        username.executa();
        password.executa();
        return logic.login(username.getOpcao(), password.getOpcao());
    }

    public void controlador(){

        boolean end = false;
        int answer;

        while (!end){
            switch (this.login()){
                case -1:
                    printer.printMsg("Crendenciais erradas! Tente outra vez!");
                    break;

                case 0:
                    printer.printMsg("Bem vindo Funcionario do Balcao xxx");
                    controladorBalcao();
                    break;

                case 1:
                    printer.printMsg("Bem vindo tecnico xxx");
                    controladorTecnico();
                    break;
                case 2:
                    printer.printMsg("Bem vindo Gestor xxx");
                    controladorGestor();
                    break;
            }
        }
        logic.encerraAplicacao();
    }
    //////////////////////////////////////Parte do Gestor////////////////////////////////////////

    private void criarConta(){
        boolean flag=false;

        MenuSelect m = new MenuSelect("Qual o tipo de funcionário que pertende criar",
                            new String[]{"Funcionario de Balcao", "Técnico de Reparacoes"});
        m.executa();


        MenuInput utilizador = new MenuInput("Insira o identificador do usuário","");
        MenuInput password = new MenuInput("Insira a palavra passe do usuário","");
        utilizador.executa();
        password.executa();

        if (m.getOpcao() == 1){
           flag =  logic.adicionaFuncBalcao(utilizador.getOpcao(),password.getOpcao());
        }
        else if (m.getOpcao()==2) {
            flag = logic.adicionaTecnico(utilizador.getOpcao(),password.getOpcao());
        }
        if (flag) printer.printMsg("Conta Adicionada com Sucesso");
        else printer.printMsg("Erro na criacao da conta");

    }




    private void controladorGestor() {
        MenuSelect gestor = new MenuSelect("",new String[]{"Criar conta para Funcionário","Estatisticas1","Estatisticas2","Estatisticas3"});
        gestor.executa();
        boolean flag=true;
        while (flag){
            gestor.executa();
            switch (gestor.getOpcao()){
              case 1:
                 criarConta();
                 break;
              case 2:
                  //TODO
                   System.out.println("ola1");
                 break;
              case 3:
                 System.out.println("ola2");
                 break;
              case 4:
                System.out.println("ola3");
                break;
              case 0:
                flag=false;
            }
        }
    }

    //////////////////////////////////////Parte do Balcao////////////////////////////////////////

    private void controladorBalcao() {
        MenuSelect balcao = new MenuSelect("",new String[]{"Criar Novo Pedido de Orcamento",
                                                                "Entregar Equipamento",
                                                                "Criar Servico Expresso",
                                                                "Criar Ficha cliente"});
        boolean flag= true;
        while (flag) {
            balcao.executa();
            switch (balcao.getOpcao()) {
                case 1:
                    criarPedidoOrcamento();
                    break;
                case 2:
                    entregarEquipamentos();
                    break;
                case 3:
                    criarServicoExpresso();
                    break;
                case 4:
                    criarFichaCliente();
                    break;
                case 0:
                    flag=false;
            }
        }

    }

    private void criarServicoExpresso() {
        MenuInput nif = new MenuInput("Nif do cliente","");
        nif.executa();
        MenuInput custos = new MenuInput("Precos Servico","");

        if(!logic.existeCliente(nif.getOpcao())){
            criarFichaCliente(nif.getOpcao());
        }

        custos.executa();
        float custo = Float.parseFloat(custos.getOpcao());
        boolean flag = true;

        flag =logic.criarServicoExpresso(custo,nif.getOpcao());
        if (flag) printer.printMsg("Servico Expresso adicionado com sucesso");
        else printer.printMsg("Erro na criacao de servico expresso");

    }

    private void criarPedidoOrcamento() {
        boolean flag;
        MenuInput nif = new MenuInput("Nif do cliente","");
        nif.executa();
        MenuInput descricao = new MenuInput("Insira a descricao do problema","");

        if(!logic.existeCliente(nif.getOpcao())){
            criarFichaCliente(nif.getOpcao());
        }

       descricao.executa();
       flag=logic.criarNovoPedido(descricao.getOpcao(),nif.getOpcao());
       if (flag) printer.printMsg("Pedido Criado");
       else printer.printMsg("Erro na criacao do pedido");
    }

    private void criarFichaCliente(){
        MenuInput nif = new MenuInput("Nif do cliente","");
        MenuInput nome =new MenuInput("Nome do cliente","");
        MenuInput email =new MenuInput("Email do cliente","");

        nif.executa();
        nome.executa();
        email.executa();
        boolean flag =logic.criaFichaCliente(nome.getOpcao(), nif.getOpcao(),email.getOpcao());
        if (flag) printer.printMsg("Ficha Criada com sucesso");
        else printer.printMsg("Erro na criacao da Ficha Cliente");

    }

    private void criarFichaCliente(String nif){
        MenuInput nome =new MenuInput("Nome do cliente","");
        MenuInput email =new MenuInput("Email do cliente","");

        nome.executa();
        email.executa();
        boolean flag =logic.criaFichaCliente(nome.getOpcao(), nif,email.getOpcao());
        if (flag) printer.printMsg("Ficha Criada com sucesso");
        else printer.printMsg("Erro na criacao da Ficha Cliente");
    }

    private void entregarEquipamentos(){
        MenuInput nif = new MenuInput("Nif do cliente","");
        nif.executa();

        List<Servico> l = logic.listarServicosProntosLevantamento(nif.getOpcao());
        boolean flag=true;
        while (flag && l.size()>0) {
            MenuSelect equip = new MenuSelect("Servicos concluidos:", (String[]) l.stream().map(s -> printer.servicoToString(s)).toArray());
            equip.executa();
            if (equip.getOpcao()==0) flag=false;
            else {
                float preco = logic.entregarEquipamento(l.get(equip.getOpcao()).getId());
                if (preco == 0) printer.printMsg("Não há custo! Entrega Concluida");
                else if (preco == -1) printer.printMsg("Erro na entrega!");
                else printer.printMsg("Custo a cobrar ao cliente: "+ preco +". Entrega Concluida");
                l = logic.listarServicosProntosLevantamento(nif.getOpcao());
            }

        }
    }

    //////////////////////////////////////Parte do Tecnico////////////////////////////////////////


    private void controladorTecnico() {
        MenuSelect tecnico = new MenuSelect("", new String[]{"Resolver Pedido",
                                                                 "Ver Pedidos de Orcamento",
                                                                 "Ver Servicos",
                                                                 "Comecar Reparacao",
                                                                 "Retomar reparações"});

        MenuSelect temp = new MenuSelect("", new String[]{"Sair"});
        boolean flag=true;
        while (flag) {
            switch (tecnico.getOpcao()) {
                case 1:
                    controladorPedidos();
                    break;
                case 2:
                    List<PedidoOrcamento> l2 = logic.listarPedidos();
                    l2.forEach(x->printer.printPedido(x));
                    temp.executa();
                    break;
                case 3:
                    List<Servico> l = logic.listarServicosPendentes();
                    l.forEach(x -> printer.printServico(x));
                    temp.executa();
                    break;
                case 4:
                    Servico id = logic.comecarServico();
                    if ( id instanceof ServicoPadrao)
                        controladorServicoPadrao(id);
                    else if (id instanceof ServicoExpresso)
                        controladorServicoExpresso(id);
                    break;
                case 5:
                    List<Servico> lSI = logic.listarServicosInterrompidos();
                    lSI.forEach(x -> printer.printServico(x));
                    MenuSelect interompdidos = new MenuSelect("Escolha um servico a retomar",(String[]) lSI.stream().map(s -> printer.servicoToString(s)).toArray() );
                    Servico s= lSI.get( interompdidos.getOpcao() -1);
                    if (s instanceof ServicoPadrao){
                        controladorServicoPadrao(s);
                    }
                    else if (s instanceof ServicoExpresso){
                        controladorServicoExpresso(s);
                    }
                    break;

                case 0:
                    flag=false;
                    break;
            }

        }
    }

    private Passo createPasso(){
        MenuInput custo = new MenuInput("Introduza o custo do passo","");
        MenuInput descr = new MenuInput("Introduza uma descrição","");
        MenuInput tempo = new MenuInput("Introduza uma duração","");
        custo.executa();
        descr.executa();
        tempo.executa();

        return new Passo(Float.parseFloat(custo.getOpcao()),descr.getOpcao(),Integer.parseInt(tempo.getOpcao()));
    }


    private void controladorServicoPadrao(Servico idS) {
        MenuSelect reparacao = new MenuSelect("Opções:", new String[]{"Adicionar Passo","Concluir Passo","Interromper Servico","Concluir Servico"});
        String id = idS.getId();
        boolean flag=true;

        printer.printServico(idS);

        while (flag) {
            List<Passo> l = logic.listarPassosServico(id);
            printer.printLPasso(l);
            reparacao.executa();
            //printer.printMsg("::::Passo Atual::::");
            //printer.printMsg(printer.passoToString(logic.proxPasso(id)));
            switch (reparacao.getOpcao()){
                case 1:
                    Passo p = createPasso();
                    logic.addPassoServico(id,p);
                    break;
                case 2:
                    //TODO
                    System.out.println("Under Construction");
                    break;
                case 3:
                    if (logic.interromperServico(id)) {
                        printer.printMsg("Serviço Interrompido com sucesso");
                        flag=false;
                    }
                    else printer.printMsg("Erro a interromper servico");
                    break;
                case 4:

                case 0:
                    MenuSelect m = new MenuSelect("Tem a certeza que pertende concluir o servico:", new String[]{"Sim", "Nao, quero voltar"});
                    m.executa();
                    if (m.getOpcao()== 1 && logic.concluiServico(id)) {
                        printer.printMsg("Serviço Concluido com sucesso");
                        flag=false;
                    }else printer.printMsg("Erro a concluir servico");
                    break;
            }


        }
    }

    private void controladorPedidos() {
       PedidoOrcamento p = logic.resolverPedido();
       List<Passo> lp = new ArrayList<>();
       boolean flag = true;
       MenuSelect criarOrcamento = new MenuSelect("Menu de Criaçao de Orçamento:", new String[]{"Adicionar Passo", "Remover Passo", "Concluir Orçamento","Rejeitar Pedido"});

       while (flag){
        printer.printLPasso(lp);   
        criarOrcamento.executa();
        switch (criarOrcamento.getOpcao()){
            case 1:
                Passo temp=createPasso();
                lp.add(temp);
                break;
            case 2:
                MenuInput n = new MenuInput("Introduza o index do passo", "");
                n.executa();
                int index =Integer.parseInt(n.getOpcao());
                if (index < lp.size() ){
                    lp.remove(index);
                    printer.printMsg("Removido com sucesso");
                }
                else printer.printMsg("Não foi possivel a remocao");
                break;
            case 4:
                MenuSelect m2 = new MenuSelect("Tem a certeza que pertende rejeitar o pedido:", new String[]{"Sim", "Nao, quero voltar"});
                m2.executa();
                if (m2.getOpcao() ==1 && logic.rejeitaPedidoOrcamento(p)){
                    flag=false;
                    printer.printMsg("Concluido Pedido");
                }
                break;
            case 3:
            case 0:
                MenuSelect m = new MenuSelect("Tem a certeza que pertende concluir o orcamento:", new String[]{"Sim", "Nao, quero voltar"});
                m.executa();
                if (m.getOpcao() ==1 && logic.criaServicoPadrao(p,lp)){
                    flag=false;
                    printer.printMsg("Concluido Pedido");
                }
                else printer.printMsg("Não foi possivel concluir orcamento");
                break;

            }
       }
       
    }

    private void controladorServicoExpresso(Servico idS){
        String id = idS.getId();
        MenuSelect m = new MenuSelect("Menu de Serviço Expresso",  new String[]{"Ver servico", "Concluir servico"});
        m.executa();
        boolean flag=true;
        while(flag){
            if (m.getOpcao()== 1){
                printer.printServico(idS);
            }
            else {
                MenuSelect m2 = new MenuSelect("Tem a certeza que pertende concluir o servico:", new String[]{"Sim", "Nao, quero voltar"});
                m2.executa();
                if (m2.getOpcao() ==1 && logic.concluiServico(id))
                    flag=false;
            }

        }
    }


}
