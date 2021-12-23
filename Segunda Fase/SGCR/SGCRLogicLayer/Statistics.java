package SGCRLogicLayer;

import SGCRDataLayer.Funcionarios.FuncionarioBalcao;
import SGCRDataLayer.Funcionarios.FuncionarioFacade;
import SGCRDataLayer.Funcionarios.Tecnico;
import SGCRDataLayer.Servicos.Servico;
import SGCRDataLayer.Servicos.ServicoPadrao;
import SGCRDataLayer.Servicos.ServicosFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Statistics {
    Map<String, TreeSet<Servico>> listaIntervencoes;
    Map<String, TecnicoStats> tecnicos;
    Map<String, BalcaoStats> balcoes;


    private Map<String, TecnicoStats> geraTecnicos(List<Tecnico> funcionarios,ServicosFacade servicosFacade){
        Map<String, TecnicoStats> tecnicos = new HashMap<>();
        return tecnicos;
    }

    private TecnicoStats generateTecnicoStats(Tecnico f,ServicosFacade servicosFacade){
        List<ServicoPadrao> servicos = servicosFacade.listarServicosConcluidos(f.getId())
                .stream()
                .filter(e-> e instanceof ServicoPadrao)
                .map(e->(ServicoPadrao) e)
                .collect(Collectors.toList());

        return new TecnicoStats(f.getnRepProgramadasConcluidas(),f.getnRepExpressoConcluidas(),
                (float) servicos.stream().mapToDouble(ServicoPadrao::duracaoPassos).average().orElse(0),
                (float) servicos.stream().mapToDouble(e->Math.abs(e.duracaoPassos()-e.duracaoPassosPrevistos())).average().orElse(0));
    }

    public static class TecnicoStats{
        int numeroReparacoesProgramadas;
        int numeroReparacoesExpresso;
        float duracaoMedia;
        float mediaDoDesvio;

        public TecnicoStats(int numeroReparacoesProgramadas, int numeroReparacoesExpresso, float duracaoMedia, float mediaDoDesvio) {
            this.numeroReparacoesProgramadas = numeroReparacoesProgramadas;
            this.numeroReparacoesExpresso = numeroReparacoesExpresso;
            this.duracaoMedia = duracaoMedia;
            this.mediaDoDesvio = mediaDoDesvio;
        }
        public int getNumeroReparacoesPadrao() {
            return numeroReparacoesProgramadas;
        }
        public int getNumeroReparacoesExpresso() {
            return numeroReparacoesExpresso;
        }
        public float getDuracaoMedia() {
            return duracaoMedia;
        }
        public float getMediaDoDesvio() {
            return mediaDoDesvio;
        }
    }

    private Map<String, BalcaoStats> geraBalcoes(List<FuncionarioBalcao> funcionarios){
        Map<String, BalcaoStats> balcoes = new HashMap<>();
        for (FuncionarioBalcao fb: funcionarios){
            balcoes.put(fb.getId(),generateBalcaoStats(fb));
        }
        return  balcoes;
    }
    private BalcaoStats generateBalcaoStats(FuncionarioBalcao f){
        return new BalcaoStats(f.getnEntregas(),f.getnRececoes());
    }

    public static class BalcaoStats {
        int entregas;
        int rececoes;

        public BalcaoStats(int entregas, int rececoes) {
            this.entregas = entregas;
            this.rececoes = rececoes;
        }
        public int getEntregas() {
            return entregas;
        }
        public int getRececoes() {
            return rececoes;
        }
    }


    public Statistics(ServicosFacade servicosFacade, FuncionarioFacade funcionarioFacade){
        this.listaIntervencoes= servicosFacade.listaIntervencoes();
        this.tecnicos = geraTecnicos(funcionarioFacade.listarTecnicos(),servicosFacade);
        this.balcoes = geraBalcoes(funcionarioFacade.listarFuncionariosBalcao());

    }
}
