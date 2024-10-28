package dat.controllers.impl;

import dat.controllers.IPlantController;
import dat.daos.impl.PlantDAOMock;
import dat.dtos.PlantDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PlantController implements IPlantController {

    private final PlantDAOMock plantDAO = new PlantDAOMock();

    @Override
    public void getAllPlants(Context ctx) throws ApiException {
        List<PlantDTO> plants = plantDAO.getAll();
        if (plants == null) {
            throw new ApiException(500, "Error fetching plants from database");
        }
        ctx.json(plants);
        ctx.status(200);
    }

    @Override
    public void getPlantById(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PlantDTO plant = plantDAO.getById(id);

            if (plant == null) {
                throw new ApiException(404, "Plant not found with id: " + id);
            }

            ctx.json(plant);
            ctx.status(200);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format");
        }
    }

    @Override
    public void getPlantsByType(Context ctx) throws ApiException {
        String type = ctx.pathParam("type");
        if (type == null || type.trim().isEmpty()) {
            throw new ApiException(400, "Plant type must be provided");
        }

        List<PlantDTO> plants = plantDAO.getByType(type);
        if (plants.isEmpty()) {
            throw new ApiException(404, "No plants found of type: " + type);
        }

        ctx.json(plants);
        ctx.status(200);
    }

    @Override
    public void createPlant(Context ctx) throws ApiException {
        try {
            PlantDTO plantDTO = ctx.bodyAsClass(PlantDTO.class);
            validatePlantDTO(plantDTO); // Using the default method from interface

            PlantDTO createdPlant = plantDAO.add(plantDTO);
            if (createdPlant == null) {
                throw new ApiException(500, "Failed to create plant");
            }

            ctx.json(createdPlant);
            ctx.status(201);
        } catch (IllegalStateException e) {
            throw new ApiException(400, "Invalid request body");
        }
    }
}