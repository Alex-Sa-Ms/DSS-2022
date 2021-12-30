package SGCRDataLayer.Funcionarios;

import java.util.ArrayList;
import java.util.List;

public interface iFuncionario {
    public int getNrTecnicos();

    /**
     *
     * @param id
     * @param password
     */
    public boolean addTecnico(String id, String password);

    /**
     *
     * @param id
     * @param password
     */
    public boolean addFuncBalcao(String id, String password);

    /**
     * @param id string que é o identificador do funcionário que se está à procura
     * @return Funcionario que se está à procura
     */
    public Funcionario getFuncionario(String id);

    /**
     * verifica se existe um funcionário com tais credenciais
     * @param id string que é o identificador do funcionário que está a ser verificado
     * @param password string que é a palavra-passe do funcionário que está a ser verificado
     * @return
     * 			-1 login incorreto
     * 			0  funcionario balcao
     * 			1  tecnico
     * 			2  gestor
     */
    public int verificaCredenciais(String id, String password);

    /**
     * aumentar o número de receções de um determinado funcionário de balcão
     * @param idFuncBalcao string que é identificador do funcionário de balcão a quem se está a incrementar o número de receções
     * @return false
     */
    public boolean incNrRececoes(String idFuncBalcao);

    /**
     * aumentar o número de entregas de um determinado funcionário de balcão
     * @param idFuncBalcao string que é identificador do funcionário de balcão a quem se está a incrementar o número de entregas
     * @return false
     */
    public boolean incNrEntregas(String idFuncBalcao);

    /**
     * adicionar o identificador de um serviço a um determinado tecnico
     * @param idTecnico string que é o identificador do tecnico a quem se está a adicionar o identificador de um serviço
     * @param idServico tring que é o identificador do serviço que está a ser adicionado
     */
    public boolean addServicoTecnico(String idTecnico, String idServico);

    /**
     * incrementa o número de reparações padrão concluidas por um determinado técnico e atualiza a duração média de reparações padrão
     * @param idTecnico string que é o identificador do tecnico a quem se está a incrementar o reparações padrão concluidas
     * @param duracao duração média da reparação padrão
     * @param duracaoPrevista duração prevista da reparação padrão
     */
    public void incNrRepProgConcluidas(String idTecnico, float duracao, float duracaoPrevista);

    /**
     * incrementa o número de reparações expresso concluidas pelo técnico
     * @param idTecnico string que é o identificador do tecnico a quem se está a incrementar o reparações expresso concluidas
     */
    public void incNrRepExpConcluidas(String idTecnico);

    /**
     * @return lista de todos os funcionários que são técnicos
     */
    public List<Tecnico> listarTecnicos();

    /**
     * @return lista de todos os funcionários que são funcionários de balcão
     */
    public List<FuncionarioBalcao> listarFuncionariosBalcao();

    /**
     * listar os serviços de um determinado técnico
     * @param idTecnico string que é o identificador do tecnico
     * @return lista de identificadores de serviço
     */
    public List<String> listarServicosTecnico(String idTecnico);

    /**
     * verifica se um determinado técnico possui um determinado serviço
     * @param idTecnico string que é o identificador do tecnico
     * @param idServico string que é o identificador do serviço
     * @return true se possuir e false se não possuir
     */
    public boolean possuiServico(String idTecnico, String idServico);
}
