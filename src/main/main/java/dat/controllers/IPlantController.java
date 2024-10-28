package dat.controllers;

import io.javalin.http.Context;

public interface IPlantController {
    void getAllPlants(Context ctx);
    void getPlantById(Context ctx);
    void getPlantsByType(Context ctx);
    void createPlant(Context ctx);
}
