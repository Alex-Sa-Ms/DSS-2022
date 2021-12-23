package SGCRLogicLayer;


import SGCRDataLayer.Funcionarios.FuncionarioFacade;
import SGCRDataLayer.Servicos.Passo;
import SGCRDataLayer.Servicos.Servico;
import SGCRDataLayer.Servicos.ServicosFacade;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Statistics {
    Map<String, TreeSet<Servico>> listaIntervencoes;
    Map<String, TecnicoStats> tecnicos;
    Map<String, BalcaoStats> balcoes;


    private TecnicoStats generateTecnicoStats(ServicosFacade servicosFacade, FuncionarioFacade funcionarioFacade,String id){

    }


    public static class TecnicoStats{
        int numeroReparacoesPadrao;
        int numeroReparacoesExpresso;
        float duracaoMedia;
        float mediaDoDesvio;
        public TecnicoStats(int numeroReparacoesPadrao, int numeroReparacoesExpresso, float duracaoMedia, float mediaDoDesvio) {
            this.numeroReparacoesPadrao = numeroReparacoesPadrao;
            this.numeroReparacoesExpresso = numeroReparacoesExpresso;
            this.duracaoMedia = duracaoMedia;
            this.mediaDoDesvio = mediaDoDesvio;
        }
        public int getNumeroReparacoesPadrao() {
            return numeroReparacoesPadrao;
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
        this.tecnicos =
    }

}
