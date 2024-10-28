package dat.controllers.impl;

import dat.controllers.IPlantController;
import dat.daos.impl.PlantDAOMock;
import dat.dtos.PlantDTO;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class PlantController implements IPlantController {

    private final PlantDAOMock plantDAO = new PlantDAOMock();

    @Override
    public void getAllPlants(Context ctx) {
        List<PlantDTO> plants = plantDAO.getAll();
        ctx.json(plants);
    }

    @Override
    public void getPlantById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PlantDTO plant = plantDAO.getById(id);
        if (plant != null) {
            ctx.json(plant);
        } else {
            ctx.status(404);
            ctx.json(Map.of("message", "Plant not found with id: " + id));
        }
    }

    @Override
    public void getPlantsByType(Context ctx) {
        String type = ctx.pathParam("type");
        List<PlantDTO> plants = plantDAO.getByType(type);
        ctx.json(plants);
    }

    @Override
    public void createPlant(Context ctx) {
        PlantDTO plant = ctx.bodyAsClass(PlantDTO.class);
        PlantDTO createdPlant = plantDAO.add(plant);
        ctx.status(201);
        ctx.json(createdPlant);
    }
}