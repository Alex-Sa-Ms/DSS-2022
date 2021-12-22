package SGCRLogicLayer;

public class Horas {
    public static float converteTimeMillisParaHoras(long tempo){
        return (float) (((double) tempo / 1000) / 3600);
    }
}
