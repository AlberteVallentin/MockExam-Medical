package dat.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dat.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for Appointment entity
 * Handles conversion from Entity to DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Integer id;
    private String clientName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private String comment;

    // Optional reference to doctor
    private Integer doctorId;
    private String doctorName;

    /**
     * Constructor for creating DTO from entity
     * @param appointment The Appointment entity to convert
     */
    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.clientName = appointment.getClientName();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.comment = appointment.getComment();

        // Include basic doctor information if available
        if (appointment.getDoctor() != null) {
            this.doctorId = appointment.getDoctor().getId();
            this.doctorName = appointment.getDoctor().getName();
        }
    }

    /**
     * Convert a list of Appointment entities to DTOs
     * @param appointments List of Appointment entities
     * @return List of AppointmentDTOs
     */
    public static List<AppointmentDTO> toDTO(List<Appointment> appointments) {
        return appointments.stream()
            .map(AppointmentDTO::new)
            .collect(Collectors.toList());
    }
}