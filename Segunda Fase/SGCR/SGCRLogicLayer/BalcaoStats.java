package SGCRLogicLayer;

public class BalcaoStats {
    String id;
    int entregas;
    int rececoes;

    public BalcaoStats(String id,int entregas, int rececoes) {
        this.id       = id;
        this.entregas = entregas;
        this.rececoes = rececoes;
    }

    public String getId()    { return id;}
    public int getEntregas() { return entregas; }
    public int getRececoes() { return rececoes; }
}
