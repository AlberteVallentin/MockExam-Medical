package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.enums.Speciality;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DoctorDAO implements IDAO<DoctorDTO, Integer> {

    private static DoctorDAO instance;
    private static EntityManagerFactory emf;

    public static DoctorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DoctorDAO();
        }
        return instance;
    }

    @Override
    public DoctorDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, id);
            return doctor != null ? new DoctorDTO(doctor) : null;
        }
    }

    @Override
    public List<DoctorDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d", Doctor.class);
            List<Doctor> doctors = query.getResultList();
            return doctors.stream()
                .map(DoctorDTO::new)
                .collect(Collectors.toList());
        }
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = new Doctor(doctorDTO);
            em.persist(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(doctor);
        }
    }

    @Override
    public DoctorDTO update(Integer id, DoctorDTO doctorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);
            doctor.updateFromDTO(doctorDTO);
            Doctor mergedDoctor = em.merge(doctor);
            em.getTransaction().commit();
            return mergedDoctor != null ? new DoctorDTO(mergedDoctor) : null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor != null) {
                em.remove(doctor);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Doctor.class, id) != null;
        }
    }

    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery(
                "SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.speciality = :speciality",
                DoctorDTO.class);
            query.setParameter("speciality", speciality);
            return query.getResultList();
        }
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery(
                "SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.dateOfBirth BETWEEN :from AND :to",
                DoctorDTO.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.getResultList();
        }
    }
}