package dat.routes;

import dat.controllers.impl.PlantController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final PlantController plantController = new PlantController();

    public EndpointGroup getRoutes() {
        return () -> {
            path("plants", () -> {
                get("/", plantController::getAllPlants);
                get("/{id}", plantController::getPlantById);
                get("/type/{type}", plantController::getPlantsByType);
                post("/", plantController::createPlant);
            });
        };
    }
}