package dat.entities;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Appointment> appointments = new ArrayList<>();

    // Add constructor that takes DoctorDTO
    public Doctor(DoctorDTO dto) {
        updateFromDTO(dto);
    }

    // Update the updateFromDTO method to handle null id
    public void updateFromDTO(DoctorDTO dto) {
        if (dto.getId() != null) {
            this.id = dto.getId();
        }
        this.name = dto.getName();
        this.dateOfBirth = dto.getDateOfBirth();
        this.yearOfGraduation = dto.getYearOfGraduation();
        this.nameOfClinic = dto.getNameOfClinic();
        this.speciality = dto.getSpeciality();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setDoctor(null);
    }
}