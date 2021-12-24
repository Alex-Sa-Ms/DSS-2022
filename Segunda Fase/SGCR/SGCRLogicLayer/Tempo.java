package SGCRLogicLayer;

public class Tempo {
    private static final double DIVISOR_PARA_HORAS = 3600000;  // 1000 milisegundos * 60 segundos * 60 minutos
    private static final double DIVISOR_PARA_DIAS  = 86400000; // 1000 milisegundos * 60 segundos * 60 minutos * 24 horas

    //Para testes
    public static float converteTimeMillisParaSegundos(long tempo){ return (float) ((double) tempo / 1000); }

    public static float converteTimeMillisParaHoras(long tempo){ return (float) ((double) tempo / DIVISOR_PARA_HORAS); }

    public static float converteTimeMillisParaDias(long tempo){
        return (float) ((double) tempo / DIVISOR_PARA_DIAS);
    }
}
