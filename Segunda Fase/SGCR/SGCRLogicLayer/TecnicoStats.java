package SGCRLogicLayer;

public class TecnicoStats {
    String id;
    int numeroReparacoesProgramadas;
    int numeroReparacoesExpresso;
    float duracaoMedia;
    float mediaDoDesvio;

    public TecnicoStats(String id,int numeroReparacoesProgramadas, int numeroReparacoesExpresso, float duracaoMedia, float mediaDoDesvio) {
        this.id                          = id;
        this.numeroReparacoesProgramadas = numeroReparacoesProgramadas;
        this.numeroReparacoesExpresso    = numeroReparacoesExpresso;
        this.duracaoMedia                = duracaoMedia;
        this.mediaDoDesvio               = mediaDoDesvio;
    }

    public String getId() {return id;}
    public int getNumeroReparacoesPadrao()   { return numeroReparacoesProgramadas; }
    public int getNumeroReparacoesExpresso() { return numeroReparacoesExpresso; }
    public float getDuracaoMedia()  { return duracaoMedia; }
    public float getMediaDoDesvio() { return mediaDoDesvio; }
}
