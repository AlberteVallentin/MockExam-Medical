package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
    }

    public static Javalin startServer(int port) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Javalin app = Javalin.create(ApplicationConfig::configuration);

        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred", e);
        ctx.status(500);
    }
}