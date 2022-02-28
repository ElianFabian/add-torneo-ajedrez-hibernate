import entidades.Jugador;
import org.hibernate.Session;

public class Calificacion
{
    static Session session;

    static void mostrarPuntos()
    {
        String asterisco = "*";
        String lineaDivisioria = asterisco.repeat(180);

        // Línea divisoria
        System.out.println("\n" + lineaDivisioria);
        
        // Se muestran los ids en columnas
        System.out.format("%5s%30s ", " ", " "); // Añade un espacio para ajustar las columnas
        for (Jugador jugador : Partidas.listaJugadores)
        {
            System.out.format("%5s ", "[" + jugador.getId() + "]");
        }
        System.out.println(" | PUNTOS TOTALES");
        
        for (int i = 0; i < Partidas.resultado.length; i++)
        {
            Jugador jugador = Partidas.listaJugadores.get(i);
            System.out.format("%4s:%25s --- ", "[" + jugador.getId() + "]", jugador.getNombre());

            // Resultado contra cada jugador (0, 1, 1/2)
            for (float celda : Partidas.resultado[i])
            {
                System.out.format("%5s ", celdaToString(celda));
                System.out.print(ConsoleColors.RESET);
            }
            // Puntos totales de cada jugador
            System.out.print(ConsoleColors.CYAN);
            System.out.println("  | " + puntosJugador(Partidas.resultado[i]));
            System.out.print(ConsoleColors.RESET);
        }

        // Línea divisoria
        System.out.println(lineaDivisioria);
    }

    /**
     * A la hora de mostrar el valor de una celda se convierte al formato de interés. 1: 1, 0: 0, 0.5: 1/2, -1: *
     */
    private static String celdaToString(float celda)
    {
        if (celda == -2)
        {
            System.out.print(ConsoleColors.BLACK_BRIGHT);
            return "X";
        }
        if (celda == -1)
        {
            System.out.print(ConsoleColors.BLACK);
            return "██";
        }
        if (celda == 0)
        {
            System.out.print(ConsoleColors.RED_BOLD);
            return "0";
        }
        if (celda == 0.5f)
        {
            System.out.print(ConsoleColors.YELLOW);
            return "1/2";
        }
        if (celda == 1)
        {
            System.out.print(ConsoleColors.GREEN_BOLD);
            return "1";
        }
        return "";
    }
    /**
     * Devuelve los puntos totales de un jugador.
     *
     * @param puntos Array que contiene los puntos de un jugador.
     */
    private static float puntosJugador(float[] puntos)
    {
        float puntosJugador = 0;

        for (float punto : puntos)
        {
            puntosJugador += punto < 0 ? 0 : punto;
        }
        return puntosJugador;
    }
}
