package dat.config;

import dat.exceptions.ApiException;
import dat.utils.Utils;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {
    private static EntityManagerFactory emf;
    private static EntityManagerFactory emfTest;
    private static Boolean isTest = false;

    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null)
            emf = createEMF(getTest());
        return emf;
    }

    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null) {
            setTest(true);
            emfTest = createEMF(getTest());
        }
        return emfTest;
    }

    private static void getAnnotationConfiguration(Configuration configuration) {
        // Add the entity classes here
        //configuration.addAnnotatedClass(Plant.class);
        //configuration.addAnnotatedClass(Reseller.class);
    }

    private static EntityManagerFactory createEMF(boolean forTest) {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();

            setBaseProperties(props);
            if (forTest) {
                props = setTestProperties(props);
            } else if (System.getenv("DEPLOYED") != null) {
                props = setDeployedProperties(props);
            } else {
                props = setDevProperties(props);
            }

            configuration.setProperties(props);
            getAnnotationConfiguration(configuration);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
            return sf.unwrap(EntityManagerFactory.class);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties setBaseProperties(Properties props) {
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        props.put("hibernate.connection.CharSet", "utf8");
        props.put("hibernate.connection.characterEncoding", "utf8");
        props.put("hibernate.connection.useUnicode", "true");
        props.put("hibernate.connection.defaultNChar", "true");
        return props;
    }

    private static Properties setDevProperties(Properties props) throws ApiException {
        String DBName = Utils.getPropertyValue("DB_NAME", "config.properties");
        String connectionString = "jdbc:postgresql://localhost:5432/" + DBName;
        connectionString += "?characterEncoding=utf8&useUnicode=true";

        props.put("hibernate.connection.url", connectionString);
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        return props;
    }

    private static Properties setDeployedProperties(Properties props) {
        String DBName = System.getenv("DB_NAME");
        String connectionString = System.getenv("CONNECTION_STR") + DBName;

        if (!connectionString.contains("?")) {
            connectionString += "?";
        } else {
            connectionString += "&";
        }
        connectionString += "characterEncoding=utf8&useUnicode=true";

        props.setProperty("hibernate.connection.url", connectionString);
        props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        return props;
    }

    private static Properties setTestProperties(Properties props) {
        props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
        props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db?characterEncoding=utf8&useUnicode=true");
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        props.put("hibernate.archive.autodetection", "class");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        return props;
    }
}