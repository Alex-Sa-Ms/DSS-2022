package SGCRLogicLayer;

public class BalcaoStats {
    int entregas;
    int rececoes;

    public BalcaoStats(int entregas, int rececoes) {
        this.entregas = entregas;
        this.rececoes = rececoes;
    }

    public int getEntregas() { return entregas; }
    public int getRececoes() { return rececoes; }
}
