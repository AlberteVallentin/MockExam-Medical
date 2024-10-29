package dat.config;

import dat.entities.Doctor;
import dat.entities.Appointment;
import dat.enums.Speciality;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Task 4.9: Database populator class
 * Creates initial doctors and appointments for testing
 */
public class Populator {

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        populate(emf);
    }

    public static void populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create first doctor
            Doctor doctor1 = new Doctor();
            doctor1.setName("Dr. Alice Smith");
            doctor1.setDateOfBirth(LocalDate.of(1975, 4, 12));
            doctor1.setYearOfGraduation(2000);
            doctor1.setNameOfClinic("City Health Clinic");
            doctor1.setSpeciality(Speciality.FAMILY_MEDICINE);

            // Add appointments for doctor1
            Appointment app1 = new Appointment();
            app1.setClientName("John Smith");
            app1.setDate(LocalDate.now().plusDays(1));
            app1.setTime(LocalTime.of(9, 45));
            app1.setComment("First visit");
            doctor1.addAppointment(app1);

            Appointment app2 = new Appointment();
            app2.setClientName("Alice Johnson");
            app2.setDate(LocalDate.now().plusDays(2));
            app2.setTime(LocalTime.of(10, 30));
            app2.setComment("Follow up");
            doctor1.addAppointment(app2);

            // Create second doctor
            Doctor doctor2 = new Doctor();
            doctor2.setName("Dr. Bob Johnson");
            doctor2.setDateOfBirth(LocalDate.of(1980, 8, 5));
            doctor2.setYearOfGraduation(2005);
            doctor2.setNameOfClinic("Downtown Medical Center");
            doctor2.setSpeciality(Speciality.SURGERY);

            // Add appointments for doctor2
            Appointment app3 = new Appointment();
            app3.setClientName("Emily White");
            app3.setDate(LocalDate.now().plusDays(3));
            app3.setTime(LocalTime.of(14, 0));
            app3.setComment("General check");
            doctor2.addAppointment(app3);

            Appointment app4 = new Appointment();
            app4.setClientName("David Martinez");
            app4.setDate(LocalDate.now().plusDays(4));
            app4.setTime(LocalTime.of(11, 0));
            app4.setComment("Consultation");
            doctor2.addAppointment(app4);

            // Persist doctors (will cascade to appointments)
            em.persist(doctor1);
            em.persist(doctor2);

            em.getTransaction().commit();

            System.out.println("Database populated with doctors and appointments!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not populate database: " + e.getMessage());
        }
    }
}
