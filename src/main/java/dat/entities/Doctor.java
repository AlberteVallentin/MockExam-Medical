package dat.entities;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Doctor in the medical clinic system
 * Includes automatic timestamp management for creation and updates
 */
@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "appointments")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private int yearOfGraduation;

    @Column(nullable = false)
    private String nameOfClinic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Speciality speciality;

    // Automatically managed temporal fields
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // One-to-Many relationship with Appointment
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Constructor for creating a doctor from DTO
     * @param dto The DoctorDTO containing the data
     */
    public Doctor(DoctorDTO dto) {
        updateFromDTO(dto);
    }

    /**
     * Update entity data from DTO
     * @param dto The DoctorDTO containing the new data
     */
    public void updateFromDTO(DoctorDTO dto) {
        this.name = dto.getName();
        this.dateOfBirth = dto.getDateOfBirth();
        this.yearOfGraduation = dto.getYearOfGraduation();
        this.nameOfClinic = dto.getNameOfClinic();
        this.speciality = dto.getSpeciality();
    }

    /**
     * Helper method to add an appointment
     * Manages both sides of the bidirectional relationship
     * @param appointment The appointment to add
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }

    /**
     * Helper method to remove an appointment
     * Manages both sides of the bidirectional relationship
     * @param appointment The appointment to remove
     */
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setDoctor(null);
    }

    /**
     * Sets the creation timestamp before persisting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the modification timestamp before updating
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}