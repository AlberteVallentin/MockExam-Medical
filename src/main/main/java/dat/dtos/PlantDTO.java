package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantDTO {
    private int id;
    private String planttype;
    private String name;
    private int maxheight;
    private double price;

    public PlantDTO(String planttype, String name, int maxheight, double price) {
        this.planttype = planttype;
        this.name = name;
        this.maxheight = maxheight;
        this.price = price;
    }
}