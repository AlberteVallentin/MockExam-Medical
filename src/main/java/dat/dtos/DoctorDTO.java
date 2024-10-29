package dat.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dat.entities.Doctor;
import dat.enums.Speciality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorDTO {
    private Integer id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private int yearOfGraduation;
    private String nameOfClinic;
    private Speciality speciality;
    private List<AppointmentDTO> appointments;

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.dateOfBirth = doctor.getDateOfBirth();
        this.yearOfGraduation = doctor.getYearOfGraduation();
        this.nameOfClinic = doctor.getNameOfClinic();
        this.speciality = doctor.getSpeciality();

        // Convert appointments if they exist
        if (doctor.getAppointments() != null && !doctor.getAppointments().isEmpty()) {
            this.appointments = doctor.getAppointments().stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        }
    }

    // Constructor for creating DTO with all basic fields
    public DoctorDTO(Integer id, String name, LocalDate dateOfBirth,
                     int yearOfGraduation, String nameOfClinic, Speciality speciality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.nameOfClinic = nameOfClinic;
        this.speciality = speciality;
    }
}