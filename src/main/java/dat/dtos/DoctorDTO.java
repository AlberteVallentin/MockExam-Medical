package dat.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dat.entities.Doctor;
import dat.enums.Speciality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for Doctor entity
 * Handles conversion from Entity to DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    private int id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private int yearOfGraduation;
    private String nameOfClinic;
    private Speciality speciality;

    // Optional field for appointments when needed
    private List<AppointmentDTO> appointments;

    /**
     * Constructor for creating DTO with all fields except appointments
     * Used primarily for mock data and testing
     */
    public DoctorDTO(int id, String name, LocalDate dateOfBirth, int yearOfGraduation,
                     String nameOfClinic, Speciality speciality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.nameOfClinic = nameOfClinic;
        this.speciality = speciality;
    }

    /**
     * Constructor for creating DTO from entity without appointments
     * @param doctor The Doctor entity to convert
     */
    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.dateOfBirth = doctor.getDateOfBirth();
        this.yearOfGraduation = doctor.getYearOfGraduation();
        this.nameOfClinic = doctor.getNameOfClinic();
        this.speciality = doctor.getSpeciality();
    }

    /**
     * Constructor for creating DTO from entity including appointments
     * @param doctor The Doctor entity to convert
     * @param includeAppointments Whether to include appointments in the conversion
     */
    public DoctorDTO(Doctor doctor, boolean includeAppointments) {
        this(doctor); // Call the basic constructor first
        if (includeAppointments && doctor.getAppointments() != null) {
            this.appointments = doctor.getAppointments().stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        }
    }

    /**
     * Convert a list of Doctor entities to DTOs
     * @param doctors List of Doctor entities
     * @return List of DoctorDTOs
     */
    public static List<DoctorDTO> toDTO(List<Doctor> doctors) {
        return doctors.stream()
            .map(DoctorDTO::new)
            .collect(Collectors.toList());
    }

    /**
     * Convert a list of Doctor entities to DTOs including appointments
     * @param doctors List of Doctor entities
     * @return List of DoctorDTOs with appointments
     */
    public static List<DoctorDTO> toDTOWithAppointments(List<Doctor> doctors) {
        return doctors.stream()
            .map(doc -> new DoctorDTO(doc, true))
            .collect(Collectors.toList());
    }
}