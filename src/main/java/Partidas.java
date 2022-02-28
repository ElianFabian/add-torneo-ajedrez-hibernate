import entidades.Jugador;
import entidades.Partida;
import org.hibernate.Session;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Partidas
{
    static Session session;

    static int partidasAJugar;
    static List<Jugador> listaJugadores;
    static List<Partida> listaPartidas;
    //static List<Integer> IDsDisponibles = new ArrayList<>();

    /**
     * Asocia cada par de jugadores (tanto al derecho como al revés) con su partida.
     * Ejemplo: (blancas1-negras1, partida1), (negras1-blancas1, partida1)
     */
    static HashMap<String, Partida> partidasHashMap = new HashMap<>();
    static float[][] resultado;

    static List<Jugador> obtenerListaJugadores()
    {
        return session.createQuery("From Jugador", Jugador.class).getResultList();
    }
    static List<Partida> obtenerListaPartidas()
    {
        return session.createQuery("From Partida", Partida.class).getResultList();
    }

    /**
     * Inserta de forma masiva las partidas necesarias hasta llegar al máximo según el número de jugadores totales y las partidas existentes.
     */
    static void insertarPartidas()
    {
        // Se añadirán las partidas correspondientes según la cantidad de jugadores
        // y el número de partidas existentes
        int nPartidasAGenerar = partidasAJugar - listaPartidas.size();

        // Se insertan partidas hasta llenar el máximo
        for (int i = 0; i < nPartidasAGenerar; )
        {
            if (insertarPartida()) i++;
        }
    }

    /**
     * Devuelve verdad si la partida ya existe.
     */
    static boolean existePartida(Partida partida)
    {
        int blancasID = partida.getBlancas().getId();
        int negrasID = partida.getNegras().getId();

        String blancas_negras = blancasID + "-" + negrasID;
        String negras_blancas = negrasID + "-" + blancasID;

        return partidasHashMap.containsKey(blancas_negras) || partidasHashMap.containsKey(negras_blancas);
    }

    /**
     * Devuelve la cantidad de partidas a jugar según el número de jugadores que haya en la base de datos.
     */
    static int partidasAJugar()
    {
        List<Jugador> jugadores = obtenerListaJugadores();

        int nJugadores = jugadores.size();

        return nJugadores * ( nJugadores - 1 ) / 2;
    }

    /**
     * Rellena con -1 las partidas invalidas (un jugador contra sí mismo).
     */
    static void establecerPartidasInvalidas()
    {
        for (int i = 0; i < resultado.length; i++)
        {
            resultado[i][i] = -1;
        }
    }

    /**
     * Se tienen en cuenta las partidas que están en la base de datos para hacer los correspondientes cálculos.
     */
    static void rellenarHashMapConPartidasExistentes()
    {
        for (var partida : listaPartidas)
        {
            int blancasID = partida.getBlancas().getId();
            int negrasID = partida.getNegras().getId();

            partidasHashMap.put(blancasID + "-" + negrasID, partida);
            partidasHashMap.put(negrasID + "-" + blancasID, partida);
        }
    }

    /**
     * Inserta una partida en caso de poderse.
     *
     * @return Devuelve verdad si la partida se ha insertado.
     */
    static boolean insertarPartida()
    {
        Partida partida = generarPartida();

        // Sólo se inserta la partida si esta no existe en la tabla
        if (!existePartida(partida))
        {
            session.save(partida);

            int blancasID = partida.getBlancas().getId();
            int negrasID = partida.getNegras().getId();

            resultado[blancasID - 1][negrasID - 1] = puntosDeEstado(partida.getGanan());
            resultado[negrasID - 1][blancasID - 1] = puntosDeEstado(invertirEstado(partida.getGanan()));

            partidasHashMap.put(blancasID + "-" + negrasID, partida);
            partidasHashMap.put(negrasID + "-" + blancasID, partida);

            listaPartidas.add(partida);
            System.out.println("OK, partida insertada.");
            return true;
        }
        System.out.println("KO, la partida ya existe.");
        return false;
    }

    /**
     * Se rellenan todas las celdas con -2 para indicar que aún no han jugado ninguna partida.
     * Al mostrar los resultados este -2 será una X.
     */
    static void establecerResultadosNulos()
    {
        for (float[] row : resultado) Arrays.fill(row, -2);
    }

    /**
     * Genera una partida con 2 jugadores y un estado de victoria aleatorios.
     */
    private static Partida generarPartida()
    {
        int listaJugadoresSize = listaJugadores.size();

        Jugador blancas = listaJugadores.get(Utiles.randomRange(0, listaJugadoresSize - 1));
        Jugador negras = listaJugadores.get(Utiles.randomRange(0, listaJugadoresSize - 1));

        // Se procura que el id de negras sea diferente al de blancas
        while (negras.getId().equals(blancas.getId()))
        {
            negras = listaJugadores.get(Utiles.randomRange(0, listaJugadoresSize - 1));
        }

        // Se genera un tiempo entre 10 min y 8 h
        int min = 10 * 60;
        int max = 8 * 60 * 60;
        int finSegundos = Utiles.randomRange(min, max);

        Partida partida = new Partida();

        partida.setBlancas(blancas);
        partida.setNegras(negras);
        partida.setGanan(EstadoVictoria.aleatorio().toString());
        partida.setInicio(Instant.now());
        partida.setFin(Instant.now().plusSeconds(finSegundos));

        return partida;
    }

    /**
     * Si un jugador A a jugado contra otro B y el resultado es B, al consultarlo al revés (de B a A) el resultado será N.
     */
    private static String invertirEstado(String estado)
    {
        switch (estado)
        {
            case "B":
                return "N";
            case "N":
                return "B";
            case "T":
                return "T";
            default:
                return "?";
        }
    }

    /**
     * Devuelve la correspondencia númerica los diferentes estados de victoria.
     *
     * @param estado B: 1, N: 0, T: 0.5
     */
    private static float puntosDeEstado(String estado)
    {
        switch (estado)
        {
            case "B":
                return 1;
            case "N":
                return 0;
            case "T":
                return 0.5f;
            default:
                return -1;
        }
    }

//    static List<Integer> obtenerIDsDisponibles() {
//        List<Integer> IDs = new ArrayList<>();
//        int nJugadores = listaJugadores.size();
//
//        for (int i = 1; i < nJugadores; i++)
//            for (int j = 1; j < nJugadores; j++) {
//                IDs.add(i);
//            }
//        return IDs;
//    }
}
