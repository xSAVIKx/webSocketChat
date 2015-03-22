package chat.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static final SessionFactory factory = buildSessionFactory();
	private static Configuration configuration;
	private static ServiceRegistry registry;

	private static SessionFactory buildSessionFactory() {
		configuration = new Configuration().configure();
		registry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();
		return configuration.buildSessionFactory(registry);
	}

	public static SessionFactory getSessionFactory() {
		return factory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
