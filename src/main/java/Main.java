import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main
{
    static Session session;
    static Transaction transaction;

    public static void main(String[] args)
    {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();

        long start = System.currentTimeMillis();
        //------------------------------------

        ejecucion();

        //------------------------------------
        long finish = System.currentTimeMillis();

        transaction.commit();
        HibernateUtil.shutdown();

        System.out.println("\nTiempo de ejecución: " + ( finish - start ) + " ms");
    }

    static void ejecucion()
    {
        // Establecemos la referencia de session para que sea usada en los métodos internos
        Partidas.session = session;
        Calificacion.session = session;

        // Se borran los registros para poder realizar pruebas
        session.createSQLQuery("TRUNCATE partidas").executeUpdate();

        Partidas.listaJugadores = Partidas.obtenerListaJugadores();
        Partidas.listaPartidas = Partidas.obtenerListaPartidas();
        Partidas.partidasAJugar = Partidas.partidasAJugar();

        // En caso de haber partidas en la base de datos se tienen en cuenta para los cálculos
        // (esto sólo tiene sentido si no se hace un TRUNCATE)
        //Partidas.rellenarHashMapConPartidasExistentes();

        // Establecemos el tamaño de la matriz de resultados según la cantidad de jugadores
        final int size = Partidas.listaJugadores.size();
        Partidas.resultado = new float[size][size];

        // Se rellena la matriz con -2 para indicar que son partidas que aún no se han jugador (en la vista se mostrarán como X)
        Partidas.establecerResultadosNulos();

        // Se rellenan con -1 las celdas de la matriz indicando que son partidas inválidas (en la vista se mostrarán con un cuadrado)
        Partidas.establecerPartidasInvalidas();

        // Se insertan partidas de forma aleatoria hasta llegar al máximo
        Partidas.insertarPartidas();
        //Partidas.insertarPartida();

        Calificacion.mostrarPuntos();
    }
}