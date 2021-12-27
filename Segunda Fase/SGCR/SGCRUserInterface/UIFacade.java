package SGCRUserInterface;
import SGCRDataLayer.Funcionarios.FuncionarioBalcao;
import SGCRDataLayer.Funcionarios.Tecnico;
import SGCRDataLayer.PedidosDeOrcamento.PedidoOrcamento;
import SGCRDataLayer.Servicos.Passo;
import SGCRDataLayer.Servicos.Servico;
import SGCRDataLayer.Servicos.ServicoExpresso;
import SGCRDataLayer.Servicos.ServicoPadrao;
import SGCRLogicLayer.BalcaoStats;
import SGCRLogicLayer.SGCRFacade;
import SGCRLogicLayer.TecnicoStats;
import SGCRLogicLayer.iSGCR;
import java.util.ArrayList;
import java.util.List;



public class UIFacade {
    private final PrintMsg printer = new PrintMsg();
    private iSGCR logic= new SGCRFacade();
    private final String predefinedPath = "file.dat";
    private String newPath = null;


    public int login(){
        printer.printMsg("Bem vindo a Sistema de Gestao do Centro de Reparacoes!");
        MenuSelect first = new MenuSelect("",new String[]{"Login", "Carregar dados de ficheiro predefinido", "Carregar dados de ficheiro personalizado","Sair e guardar em ficheiro personalizado"});

        while(true){
            first.executa();
            switch (first.getOpcao()){
                case 1:
                    MenuInput username = new MenuInput("Por favor insira o seu Username","Username:");
                    MenuInput password = new MenuInput("Por favor insira a sua palavra passe", "Password:");
                    username.executa();
                    password.executa();
                    return logic.login(username.getOpcao(), password.getOpcao());

                case 2:
                    int ret1 = logic.load(predefinedPath);
                    if (ret1 == 0) {
                        printer.printMsg("Load concluido com sucesso");
                    }
                    else printer.printMsg("Erro no load");
                    break;
                case 3:
                    MenuInput filepath2 = new MenuInput("Insira o ficheiro de onde pretende carregar os dados","");
                    filepath2.executa();
                    newPath = filepath2.getOpcao();
                    int ret2 = logic.load(newPath);
                    if (ret2 == 0) {
                        printer.printMsg("Load concluido com sucesso");
                    }
                    else printer.printMsg("Erro no load");
                    break;
                case 0:
                    return 90;
                case 4:
                    MenuInput filepath = new MenuInput("Insira o ficheiro onde pretende que sejam guardados os ficheiros","");
                    filepath.executa();
                    newPath = filepath.getOpcao();
                    return 91;
            }
        }
    }

    public void controlador(){

        boolean end = false;
        int retval=0;

        while (!end){
            switch (retval =this.login()){
                case -1:
                    printer.printMsg("Crendenciais erradas! Tente outra vez!");
                    break;

                case 0:
                    printer.printMsg("Bem vindo Funcionario do Balcao");
                    controladorBalcao();
                    break;

                case 1:
                    printer.printMsg("Bem vindo tecnico");
                    controladorTecnico();
                    break;
                case 2:
                    printer.printMsg("Bem vindo Gestor");
                    controladorGestor();
                    break;
                case 90:
                case 91:
                    printer.printMsg("A aplicacao ira ser encerrada");
                    end=true;
                    break;
            }
        }
        printer.printMsg("Guardando os dados...");
        if (retval == 90){
            logic.encerraAplicacao(predefinedPath);
        }
        else logic.encerraAplicacao(newPath);
    }
    //////////////////////////////////////Parte do Gestor////////////////////////////////////////

    private void criarConta(){
        boolean flag=false;

        MenuSelect m = new MenuSelect("Qual o tipo de funcionario que pertende criar",
                new String[]{"Funcionario de Balcao", "Tecnico de Reparacoes"});
        m.executa();


        MenuInput utilizador = new MenuInput("Insira o identificador do usuario","");
        MenuInput password = new MenuInput("Insira a palavra passe do usuario","");
        utilizador.executa();
        password.executa();

        if (m.getOpcao() == 1){
            flag =  logic.adicionaFuncBalcao(utilizador.getOpcao(),password.getOpcao());
        }
        else if (m.getOpcao()==2) {
            flag = logic.adicionaTecnico(utilizador.getOpcao(),password.getOpcao());
        }
        if (flag) printer.printMsg("Conta Adicionada com Sucesso");
        else printer.printMsg("Erro na criacao da conta (Pode já existir este utilizador) ");

    }




    private void controladorGestor() {
        MenuSelect gestor = new MenuSelect("",new String[]{
                "Criar conta para Funcionario",
                "Intervencoes Concluidas",
                "Estatisticas dos Tecnicos:",
                "Rececoes e entregas",
                "Listar Funcionarios"});
        MenuSelect temp = new MenuSelect("", new String[]{});
        boolean flag=true;
        while (flag){
            gestor.executa();
            switch (gestor.getOpcao()){
                case 1:
                    criarConta();
                    break;
                case 2:
                    printer.printMsg("Intervencoes por técnico:");
                    printer.printIntervencoes(logic.listaIntervencoes());
                    temp.executa();
                    break;
                case 3:
                    printer.printMsg("Estatisticas dos Tecnicos: ");
                    List<TecnicoStats> t = logic.estatisticasEficienciaCentro();
                    t.forEach(printer::printTecnicoStats);
                    temp.executa();
                    break;
                case 4:
                    printer.printMsg("Rececoes e entregas dos Funcionarios de Balcao");
                    List<BalcaoStats> l = logic.rececoes_e_entregas();
                    l.forEach(printer::printBalcaoStats);
                    temp.executa();
                    break;
                case 5:
                    MenuSelect func = new MenuSelect("Escolha o tipo de funcionario", new String[]{"Funcionarios de balcao","Tecnicos"});
                    func.executa();
                    if(func.getOpcao() == 1){
                        List<FuncionarioBalcao> l1 = logic.listarFuncionariosBalcao();
                        l1.forEach(x->printer.printFuncionario(x));
                    }
                    if(func.getOpcao() == 2){
                        List<Tecnico> l2 = logic.listarTecnicos();
                        l2.forEach(x->printer.printFuncionario(x));
                    }
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
                "Criar Ficha cliente",
                "Resposta Orçamento do Cliente",
                "Listar Fichas Cliente do Sistema"});
        MenuSelect temp = new MenuSelect("", new String[]{});
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
                case 5:
                    confirmarOrcamento();
                    break;
                case 6:
                    printer.printLFichaCliente(logic.listarFichasCLiente());
                    temp.executa();
                    break;
                case 0:
                    flag=false;
            }
        }

    }

    private void confirmarOrcamento() {

        MenuSelect menu2 = new MenuSelect("", new String[]{"Confirmar Orcamento", "Recusar Orcamento"});
        boolean flag=true;
        boolean temp;
        int choice=0;
        while(flag){
            List<Servico> l = logic.listarServicosEmEsperaDeConfirmacao();
            MenuSelect menu1 = new MenuSelect("Escolha o servico",  l.stream().map(s -> printer.servicoToString(s)).toArray(String[]::new));
            menu1.executa();
            Servico s = null;
            if (menu1.getOpcao()!=0 && (s=l.get(menu1.getOpcao()-1)) != null){
                while (choice == 0){
                    menu2.executa();
                    choice=menu2.getOpcao();
                }
                if (choice ==1) {
                    temp =logic.aceitarOrcamento(s.getId());
                    if (temp)printer.printMsg("Orcamento Aceite!");
                    else printer.printMsg("Erro na confirmacao do orcamento");
                }
                else {
                    temp =logic.rejeitarOrcamento(s.getId());
                    if (temp)printer.printMsg("Orcamento Recusado!");
                    else printer.printMsg("Erro na recusa do orcamento");
                }

            }else if(menu1.getOpcao() != 0){
                printer.printMsg("Erro na confirmacao do orcamento");

            }else if (menu1.getOpcao() == 0) flag=false;


        }
    }

    private void criarServicoExpresso() {
        MenuInput nif = new MenuInput("NIF do cliente","");
        nif.executa();
        MenuInput custos = new MenuInput("Precos Servico","");
        MenuInput descr = new MenuInput("Descrição:","");

        if(!logic.existeCliente(nif.getOpcao())){
            criarFichaCliente(nif.getOpcao());
        }
        boolean idiotflag;
        float custo = 0;
        
        do{
            custos.executa();
            try {
                custo = Float.parseFloat(custos.getOpcao());
                idiotflag = false;
            }catch (NumberFormatException e){
                printer.printMsg("Introduza um numero");
                idiotflag = true;
            }
        }while(idiotflag);
        
        boolean retflag;
        descr.executa();
        retflag =logic.criarServicoExpresso(custo,nif.getOpcao(), descr.getOpcao());
        if (retflag) printer.printMsg("Servico Expresso adicionado com sucesso");
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
            MenuSelect equip = new MenuSelect("Servicos concluidos:", l.stream().map(printer::servicoToString).toArray(String[]::new));
            equip.executa();
            if (equip.getOpcao()==0) flag=false;
            else {
                String idServ = l.get(equip.getOpcao()-1).getId();
                float preco = logic.entregarEquipamento(idServ);
                if (preco == 0) printer.printMsg("Nao ha custo! Entrega Concluida");
                else if (preco == -1) printer.printMsg("Erro na entrega!");
                else {
                    printer.printMsg("Custo a cobrar ao cliente: "+ preco +". Entrega Concluida");
                }
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
                "Retomar reparacoes"});

        MenuSelect temp = new MenuSelect("", new String[]{});
        boolean flag=true;
        while (flag) {
            tecnico.executa();
            switch (tecnico.getOpcao()) {
                case 1:
                    controladorPedidos();
                    break;
                case 2:
                    List<PedidoOrcamento> l2 = logic.listarPedidos();
                    l2.forEach(printer::printPedido);
                    temp.executa();
                    break;
                case 3:
                    List<Servico> l = logic.listarServicosPendentes();
                    l.forEach(printer::printServico);
                    temp.executa();
                    break;
                case 4:
                    Servico id = logic.comecarServico();
                    if ( id instanceof ServicoPadrao)
                        controladorServicoPadrao(id,false);
                    else if (id instanceof ServicoExpresso)
                        controladorServicoExpresso(id);
                    break;
                case 5:
                    List<Servico> lSI = logic.listarServicosInterrompidos();
                    MenuSelect interrompidos = new MenuSelect("Escolha um servico a retomar", lSI.stream().map(printer::servicoToString).toArray(String[]::new));
                    interrompidos.executa();
                    if (interrompidos.getOpcao() != 0 ) {
                        Servico s= lSI.get( interrompidos.getOpcao() -1);
                        if (s instanceof ServicoPadrao){
                            logic.retomarServico(s.getId());
                            controladorServicoPadrao(s,true);
                        }
                        else if (s instanceof ServicoExpresso){
                            controladorServicoExpresso(s);
                        }}
                    break;
                case 0:
                    flag=false;
                    break;
            }

        }
    }

    private Passo createPasso(){
        MenuInput custoM = new MenuInput("Introduza o custo do passo","");
        MenuInput descrM = new MenuInput("Introduza uma descricao","");
        MenuInput tempoM = new MenuInput("Introduza uma duracao","");

        boolean idiotflag;
        float custo = 0;
        int tempo=0;

        descrM.executa();
        do{
            custoM.executa();
            try {
                custo = Float.parseFloat(custoM.getOpcao());
                idiotflag = false;
            }catch (NumberFormatException e){
                printer.printMsg("Introduza um custo em numerario");
                idiotflag = true;
            }
        }while(idiotflag);

        do{
            tempoM.executa();
            try {
                tempo = Integer.parseInt(tempoM.getOpcao());
                idiotflag = false;
            }catch (NumberFormatException e){
                printer.printMsg("Introduza o tempo em minutos");
                idiotflag = true;
            }
        }while(idiotflag);

        return new Passo(custo,descrM.getOpcao(),tempo);
    }


    private void controladorServicoPadrao(Servico idS, boolean retomado) {
        MenuSelect reparacao = new MenuSelect("Opcoes:", new String[]{"Adicionar Passo","Concluir Passo","Interromper Servico","Concluir Servico"});
        String id = idS.getId();
        boolean flag=true;

        printer.printServico(idS);

        if (!retomado) {
            try {
                logic.proxPasso(id);
            } catch (iSGCR.CustoExcedidoException e) {
                printer.printMsg("Limite de Custo ultrapassado!");
            }
        }
        while (flag) {
            List<Passo> l = logic.listarPassosServico(id);
            printer.printLPasso(l);
            reparacao.executa();
            switch (reparacao.getOpcao()){
                case 1:
                    Passo p = createPasso();
                    logic.addPassoServico(id,p);
                    break;
                case 2:
                    try {
                        if (logic.proxPasso(id) != null) printer.printMsg("Passo concluido");
                        else printer.printMsg("Não existem mais passos");
                    } catch (iSGCR.CustoExcedidoException e) {
                        printer.printMsg("Limite de Custo ultrapassado!");
                    }
                    break;
                case 3:
                    if (logic.interromperServico(id)) {
                        printer.printMsg("Servico Interrompido com sucesso");
                        flag=false;
                    }
                    else printer.printMsg("Erro a interromper servico");
                    break;
                case 4:

                case 0:
                    MenuSelect m = new MenuSelect("Tem a certeza que pertende concluir o servico:", new String[]{"Sim", "Nao, quero voltar"});
                    m.executa();
                    if (m.getOpcao()== 1 && logic.concluiServico(id)) {
                        printer.printMsg("Servico Concluido com sucesso");
                        flag=false;
                    }else printer.printMsg("Erro a concluir servico");
                    break;
            }


        }
    }

    private void controladorPedidos() {
        PedidoOrcamento p = logic.resolverPedido();
        if (p == null) printer.printMsg("Não existem pedidos pendentes");
        else {
            List<Passo> lp = new ArrayList<>();
            boolean flag = true;
            MenuSelect criarOrcamento = new MenuSelect("Menu de Criacao de Orcamento:", new String[]{"Adicionar Passo", "Remover Passo", "Concluir Orcamento", "Rejeitar Pedido"});
            printer.printPedido(p);
            while (flag) {
                printer.printLPasso(lp);
                criarOrcamento.executa();
                switch (criarOrcamento.getOpcao()) {
                    case 1:
                        Passo temp = createPasso();
                        lp.add(temp);
                        break;
                    case 2:
                        MenuInput n = new MenuInput("Introduza o index do passo", "");
                        int index = 0;
                        boolean idiotflag;
                        do {
                            n.executa();
                            try {
                                index = Integer.parseInt(n.getOpcao());
                                idiotflag = false;
                            } catch (NumberFormatException e) {
                                printer.printMsg("Introduza o index");
                                idiotflag = true;
                            }
                        } while (idiotflag);
                        if (index < lp.size()) {
                            lp.remove(index-1);
                            printer.printMsg("Removido com sucesso");
                        } else printer.printMsg("Nao foi possivel a remocao");
                        break;
                    case 4:
                        MenuSelect m2 = new MenuSelect("Tem a certeza que pertende rejeitar o pedido:", new String[]{"Sim", "Nao, quero voltar"});
                        m2.executa();
                        if (m2.getOpcao() == 1 && logic.rejeitaPedidoOrcamento(p)) {
                            flag = false;
                            printer.printMsg("Concluido Pedido");
                        }
                        break;
                    case 3:
                    case 0:
                        MenuSelect m = new MenuSelect("Tem a certeza que pertende concluir o orcamento:", new String[]{"Sim", "Nao, quero voltar"});
                        m.executa();
                        if (m.getOpcao() == 1 && logic.criaServicoPadrao(p, lp)) {
                            flag = false;
                            printer.printMsg("Concluido Pedido");
                        } else printer.printMsg("Nao foi possivel concluir orcamento");
                        break;

                }
            }
        }
    }

    private void controladorServicoExpresso(Servico idS){
        String id = idS.getId();
        MenuSelect m = new MenuSelect("Menu de Servico Expresso",  new String[]{"Ver servico", "Concluir servico"});

        boolean flag=true;
        while(flag){
            m.executa();
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
