package dat.dtos;

import dat.enums.Speciality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private int yearOfGraduation;
    private String nameOfClinic;
    private Speciality speciality;
}