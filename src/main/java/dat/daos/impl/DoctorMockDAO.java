package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mock DAO implementation for doctors
 */
public class DoctorMockDAO implements IDAO<DoctorDTO, Integer> {
    private static final List<DoctorDTO> doctors = new ArrayList<>();
    private static int nextId = 1;

    static {
        doctors.add(new DoctorDTO(nextId++, "Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE));
        doctors.add(new DoctorDTO(nextId++, "Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY));
        doctors.add(new DoctorDTO(nextId++, "Dr. Clara Lee", LocalDate.of(1983, 7, 22), 2008, "Green Valley Hospital", Speciality.PEDIATRICS));
        doctors.add(new DoctorDTO(nextId++, "Dr. David Park", LocalDate.of(1978, 11, 15), 2003, "Hillside Medical Practice", Speciality.PSYCHIATRY));
        doctors.add(new DoctorDTO(nextId++, "Dr. Emily White", LocalDate.of(1982, 9, 30), 2007, "Metro Health Center", Speciality.GERIATRICS));
        doctors.add(new DoctorDTO(nextId++, "Dr. Fiona Martinez", LocalDate.of(1985, 2, 17), 2010, "Riverside Wellness Clinic", Speciality.SURGERY));
        doctors.add(new DoctorDTO(nextId++, "Dr. George Kim", LocalDate.of(1979, 5, 29), 2004, "Summit Health Institute", Speciality.FAMILY_MEDICINE));
    }

    @Override
    public DoctorDTO read(Integer id) {
        return doctors.stream()
            .filter(d -> d.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<DoctorDTO> readAll() {
        return new ArrayList<>(doctors);
    }

    @Override
    public DoctorDTO create(DoctorDTO doctor) {
        doctor.setId(nextId++);
        doctors.add(doctor);
        return doctor;
    }

    @Override
    public DoctorDTO update(Integer id, DoctorDTO doctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId() == id) {
                doctor.setId(id);
                doctors.set(i, doctor);
                return doctor;
            }
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        doctors.removeIf(d -> d.getId() == id);
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return doctors.stream().anyMatch(d -> d.getId() == id);
    }

    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        return doctors.stream()
            .filter(d -> d.getSpeciality() == speciality)
            .collect(Collectors.toList());
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        return doctors.stream()
            .filter(d -> !d.getDateOfBirth().isBefore(from) && !d.getDateOfBirth().isAfter(to))
            .collect(Collectors.toList());
    }
}