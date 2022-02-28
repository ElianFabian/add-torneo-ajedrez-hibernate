import java.util.Random;

public enum EstadoVictoria {
    B, N, T;

    private static final Random rnd = new Random();
    public static EstadoVictoria aleatorio() {
        return values()[rnd.nextInt(values().length)];
    }
}
