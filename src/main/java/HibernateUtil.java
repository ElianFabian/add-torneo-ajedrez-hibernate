import entidades.Jugador;
import entidades.Partida;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// https://www.boraji.com/hibernate-5-remove-an-entity-example#:~:text=In%20Hibernate%2C%20an%20entity%20can,and%20Session%23remove()%20methods

// https://stackoverflow.com/questions/5862680/whats-the-difference-between-session-persist-and-session-save-in-hibernate

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Crear registro
                registry = new StandardServiceRegistryBuilder()
                        .configure()
                        .build();

                // Crear MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Crear Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Crear SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
