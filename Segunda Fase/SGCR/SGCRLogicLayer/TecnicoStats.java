package SGCRLogicLayer;

public class TecnicoStats {
    int numeroReparacoesProgramadas;
    int numeroReparacoesExpresso;
    float duracaoMedia;
    float mediaDoDesvio;

    public TecnicoStats(int numeroReparacoesProgramadas, int numeroReparacoesExpresso, float duracaoMedia, float mediaDoDesvio) {
        this.numeroReparacoesProgramadas = numeroReparacoesProgramadas;
        this.numeroReparacoesExpresso    = numeroReparacoesExpresso;
        this.duracaoMedia                = duracaoMedia;
        this.mediaDoDesvio               = mediaDoDesvio;
    }

    public int getNumeroReparacoesPadrao()   { return numeroReparacoesProgramadas; }
    public int getNumeroReparacoesExpresso() { return numeroReparacoesExpresso; }
    public float getDuracaoMedia()  { return duracaoMedia; }
    public float getMediaDoDesvio() { return mediaDoDesvio; }
}
