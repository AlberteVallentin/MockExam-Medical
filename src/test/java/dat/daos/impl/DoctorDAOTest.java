package dat.daos.impl;

import dat.config.HibernateConfig;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DoctorDAO using TDD approach
 * Task 5: Testing DoctorDAO with TDD
 */
class DoctorDAOTest {
    private static EntityManagerFactory emf;
    private static DoctorDAO dao;
    private static Doctor testDoctor1;
    private static Doctor testDoctor2;

    @BeforeAll
    static void setUpClass() {
        // Use test configuration
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = DoctorDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        // Clear database and create test data
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Clear all doctors
            em.createQuery("DELETE FROM Doctor").executeUpdate();

            // Create test doctors
            testDoctor1 = new Doctor();
            testDoctor1.setName("Dr. Test One");
            testDoctor1.setDateOfBirth(LocalDate.of(1975, 4, 12));
            testDoctor1.setYearOfGraduation(2000);
            testDoctor1.setNameOfClinic("Test Clinic One");
            testDoctor1.setSpeciality(Speciality.FAMILY_MEDICINE);
            em.persist(testDoctor1);

            testDoctor2 = new Doctor();
            testDoctor2.setName("Dr. Test Two");
            testDoctor2.setDateOfBirth(LocalDate.of(1980, 8, 5));
            testDoctor2.setYearOfGraduation(2005);
            testDoctor2.setNameOfClinic("Test Clinic Two");
            testDoctor2.setSpeciality(Speciality.SURGERY);
            em.persist(testDoctor2);

            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Doctor").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Test reading a doctor by ID")
    void testRead() {
        // Arrange
        Integer id = testDoctor1.getId();

        // Act
        DoctorDTO found = dao.read(id);

        // Assert
        assertNotNull(found);
        assertEquals(testDoctor1.getName(), found.getName());
        assertEquals(testDoctor1.getSpeciality(), found.getSpeciality());
    }

    @Test
    @DisplayName("Test reading non-existent doctor")
    void testReadNonExistent() {
        // Act
        DoctorDTO found = dao.read(999999);

        // Assert
        assertNull(found);
    }

    @Test
    @DisplayName("Test reading all doctors")
    void testReadAll() {
        // Act
        List<DoctorDTO> doctors = dao.readAll();

        // Assert
        assertEquals(2, doctors.size());
        assertTrue(doctors.stream().anyMatch(d -> d.getName().equals(testDoctor1.getName())));
        assertTrue(doctors.stream().anyMatch(d -> d.getName().equals(testDoctor2.getName())));
    }

    @Test
    @DisplayName("Test creating a new doctor")
    void testCreate() throws ApiException {
        // Arrange
        DoctorDTO newDoctor = new DoctorDTO();
        newDoctor.setName("Dr. New");
        newDoctor.setDateOfBirth(LocalDate.of(1985, 1, 1));
        newDoctor.setYearOfGraduation(2010);
        newDoctor.setNameOfClinic("New Clinic");
        newDoctor.setSpeciality(Speciality.PEDIATRICS);

        // Act
        DoctorDTO created = dao.create(newDoctor);

        // Assert
        assertNotNull(created.getId());
        assertEquals(newDoctor.getName(), created.getName());
        assertEquals(newDoctor.getSpeciality(), created.getSpeciality());

        // Verify in database
        DoctorDTO found = dao.read(created.getId());
        assertNotNull(found);
        assertEquals(created.getName(), found.getName());
    }

    @Test
    @DisplayName("Test updating a doctor")
    void testUpdate() throws ApiException {
        // Arrange
        DoctorDTO updateData = new DoctorDTO();
        updateData.setName(testDoctor1.getName());
        updateData.setDateOfBirth(testDoctor1.getDateOfBirth());
        updateData.setYearOfGraduation(2001); // Changed
        updateData.setNameOfClinic("Updated Clinic"); // Changed
        updateData.setSpeciality(testDoctor1.getSpeciality());

        // Act
        DoctorDTO updated = dao.update(testDoctor1.getId(), updateData);

        // Assert
        assertNotNull(updated);
        assertEquals(2001, updated.getYearOfGraduation());
        assertEquals("Updated Clinic", updated.getNameOfClinic());

        // Verify in database
        DoctorDTO found = dao.read(testDoctor1.getId());
        assertEquals("Updated Clinic", found.getNameOfClinic());
    }

    @Test
    @DisplayName("Test finding doctors by speciality")
    void testDoctorBySpeciality() {
        // Act
        List<DoctorDTO> surgeons = dao.doctorBySpeciality(Speciality.SURGERY);

        // Assert
        assertEquals(1, surgeons.size());
        assertEquals(testDoctor2.getName(), surgeons.get(0).getName());
    }

    @Test
    @DisplayName("Test finding doctors by birthdate range")
    void testDoctorByBirthdateRange() {
        // Arrange
        LocalDate from = LocalDate.of(1970, 1, 1);
        LocalDate to = LocalDate.of(1979, 12, 31);

        // Act
        List<DoctorDTO> doctors = dao.doctorByBirthdateRange(from, to);

        // Assert
        assertEquals(1, doctors.size());
        assertEquals(testDoctor1.getName(), doctors.get(0).getName());
    }

    @Test
    @DisplayName("Test validating primary key")
    void testValidatePrimaryKey() {
        // Act & Assert
        assertTrue(dao.validatePrimaryKey(testDoctor1.getId()));
        assertFalse(dao.validatePrimaryKey(999999));
    }
}