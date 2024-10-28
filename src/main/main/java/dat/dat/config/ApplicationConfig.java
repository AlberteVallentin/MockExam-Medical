package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.routes.Routes;
import dat.exceptions.ApiException;
import dat.controllers.impl.ExceptionController;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static ExceptionController exceptionController = new ExceptionController();

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes");
        config.router.contextPath = "/api";
        config.router.apiBuilder(routes.getRoutes());
    }

    public static Javalin startServer(int port) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Javalin app = Javalin.create(ApplicationConfig::configuration);

        // Register exception handlers
        app.exception(ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(Exception.class, exceptionController::exceptionHandler);

        app.start(port);
        return app;
    }
}