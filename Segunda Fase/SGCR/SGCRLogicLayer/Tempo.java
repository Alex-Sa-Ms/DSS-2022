package SGCRLogicLayer;

public class Tempo {
    private static final double DIVISOR_PARA_SEGUNDOS = 1000;                       // 1000 milisegundos
    private static final double DIVISOR_PARA_MINUTOS  = DIVISOR_PARA_SEGUNDOS * 60; // 1000 milisegundos * 60 segundos
    private static final double DIVISOR_PARA_HORAS    = DIVISOR_PARA_MINUTOS * 60;  // 1000 milisegundos * 60 segundos * 60 minutos
    private static final double DIVISOR_PARA_DIAS     = DIVISOR_PARA_HORAS * 24;    // 1000 milisegundos * 60 segundos * 60 minutos * 24 horas

    public static float converteTimeMillisParaSegundos(long tempo){ return (float) ((double) tempo / DIVISOR_PARA_SEGUNDOS); }

    public static float converteTimeMillisParaMinutos(long tempo){ return (float) ((double) tempo / DIVISOR_PARA_MINUTOS); }

    public static float converteTimeMillisParaHoras(long tempo){ return (float) ((double) tempo / DIVISOR_PARA_HORAS); }

    public static float converteTimeMillisParaDias(long tempo){
        return (float) ((double) tempo / DIVISOR_PARA_DIAS);
    }
}
