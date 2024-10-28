package dat.daos.impl;

import dat.dtos.PlantDTO;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PlantDAOMock {
    private static final Map<Integer, PlantDTO> plants = new HashMap<>();
    private static int nextId = 1;

    static {
        plants.put(nextId++, new PlantDTO(1, "Rose", "Albertine", 400, 199.50));
        plants.put(nextId++, new PlantDTO(2, "Bush", "Aronia", 200, 169.50));
        plants.put(nextId++, new PlantDTO(3, "FruitAndBerries", "AromaApple", 350, 399.50));
        plants.put(nextId++, new PlantDTO(4, "Rhododendron", "Astrid", 40, 269.50));
        plants.put(nextId++, new PlantDTO(5, "Rose", "The DarkLady", 100, 199.50));
    }

    public List<PlantDTO> getAll() {
        return new ArrayList<>(plants.values());
    }

    public PlantDTO getById(int id) {
        return plants.get(id);
    }

    public List<PlantDTO> getByType(String type) {
        return plants.values().stream()
            .filter(p -> p.getPlanttype().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }

    public PlantDTO add(PlantDTO plant) {
        plant.setId(nextId++);
        plants.put(plant.getId(), plant);
        return plant;
    }
}