package dat.controllers;

import dat.dtos.PlantDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;

public interface IPlantController {
    void getAllPlants(Context ctx) throws ApiException;
    void getPlantById(Context ctx) throws ApiException;
    void getPlantsByType(Context ctx) throws ApiException;
    void createPlant(Context ctx) throws ApiException;

    default void validatePlantDTO(PlantDTO plant) throws ApiException {
        if (plant == null) {
            throw new ApiException(400, "Plant data is required");
        }
        if (plant.getPlanttype() == null || plant.getPlanttype().trim().isEmpty()) {
            throw new ApiException(422, "Plant type is required");
        }
        if (plant.getName() == null || plant.getName().trim().isEmpty()) {
            throw new ApiException(422, "Plant name is required");
        }
        if (plant.getMaxheight() <= 0) {
            throw new ApiException(422, "Maximum height must be greater than 0");
        }
        if (plant.getPrice() <= 0) {
            throw new ApiException(422, "Price must be greater than 0");
        }
    }
}