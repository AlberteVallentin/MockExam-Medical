package dat.controllers.impl;

import dat.controllers.IController;
import dat.daos.impl.DoctorMockDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import dat.exceptions.Message;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller handling all doctor-related HTTP endpoints
 * Implements basic CRUD operations and specialized doctor queries
 */
public class DoctorMockController implements IController<DoctorDTO, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorMockController.class);
    private final DoctorMockDAO dao;

    // Initialize controller with DAO instance
    public DoctorMockController() {
        this.dao = new DoctorMockDAO();
    }

    /**
     * Retrieves a specific doctor by their ID
     * @param ctx Javalin context containing the ID parameter
     * @throws ApiException with 404 if doctor not found
     */
    @Override
    public void read(Context ctx) {
        try {
            // Extract and validate ID from path parameter
            int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

            DoctorDTO doctorDTO = dao.read(id);
            if (doctorDTO == null) {
                throw new ApiException(404, "Doctor not found with id: " + id);
            }

            // Return successful response
            ctx.status(200);
            ctx.json(doctorDTO, DoctorDTO.class);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Retrieves all doctors in the system
     * @param ctx Javalin context for returning the response
     */
    @Override
    public void readAll(Context ctx) {
        try {
            List<DoctorDTO> doctorDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(doctorDTOS, DoctorDTO.class);
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Message(500, "Error fetching doctors: " + e.getMessage()));
        }
    }

    /**
     * Creates a new doctor
     * @param ctx Javalin context containing the doctor data in request body
     * @throws ApiException with 400 if validation fails
     */
    @Override
    public void create(Context ctx) {
        try {
            // Validate request body and create new doctor
            DoctorDTO jsonRequest = validateEntity(ctx);
            DoctorDTO doctorDTO = dao.create(jsonRequest);

            // Return created response
            ctx.status(201);
            ctx.json(doctorDTO, DoctorDTO.class);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Updates an existing doctor's information
     * @param ctx Javalin context containing the ID parameter and updated doctor data
     * @throws ApiException if doctor not found or validation fails
     */
    @Override
    public void update(Context ctx) {
        try {
            // Extract ID and validate updated doctor data
            int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

            DoctorDTO doctorDTO = dao.update(id, validateEntity(ctx));
            ctx.status(200);
            ctx.json(doctorDTO, DoctorDTO.class);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Deletes a doctor by their ID
     * @param ctx Javalin context containing the ID parameter
     */
    @Override
    public void delete(Context ctx) {
        try {
            // Extract and validate ID
            int id = ctx.pathParamAsClass("id", Integer.class)
                .get();

            // Explicitly check ID and throw ApiException if invalid
            if (!validatePrimaryKey(id)) {
                throw new ApiException(404, "Doctor with ID " + id + " not found");
            }

            dao.delete(id);
            ctx.status(204);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Finds doctors by their medical speciality
     * @param ctx Javalin context containing the speciality parameter
     * @throws ApiException with 404 if no doctors found with given speciality
     */
    public void readBySpeciality(Context ctx) {
        try {
            Speciality speciality = ctx.pathParamAsClass("speciality", Speciality.class)
                .check(s -> s != null, "Invalid speciality")
                .get();

            List<DoctorDTO> doctors = dao.doctorBySpeciality(speciality);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found with speciality: " + speciality);
            }

            ctx.status(200);
            ctx.json(doctors, DoctorDTO.class);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Finds doctors born within a specific date range
     * @param ctx Javalin context containing the date range parameters
     * @throws ApiException with 400 if date range is invalid
     * @throws ApiException with 404 if no doctors found in range
     */
    public void readByBirthdateRange(Context ctx) {
        try {
            LocalDate from = ctx.queryParamAsClass("from", LocalDate.class)
                .check(d -> d != null, "From date must be provided")
                .get();
            LocalDate to = ctx.queryParamAsClass("to", LocalDate.class)
                .check(d -> d != null, "To date must be provided")
                .get();

            if (from.isAfter(to)) {
                throw new ApiException(400, "From date cannot be after to date");
            }

            List<DoctorDTO> doctors = dao.doctorByBirthdateRange(from, to);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found in the specified date range");
            }

            ctx.status(200);
            ctx.json(doctors, DoctorDTO.class);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    /**
     * Validates if a given doctor ID exists
     * @param id The ID to validate
     * @return true if the ID exists, false otherwise
     */
    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    /**
     * Validates doctor entity data from request body
     * @param ctx Javalin context containing the doctor data
     * @return Validated DoctorDTO object
     * @throws ApiException if validation fails
     */
    @Override
    public DoctorDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(DoctorDTO.class)
            .check(d -> d.getName() != null && !d.getName().isEmpty(), "Doctor name must be set")
            .check(d -> d.getDateOfBirth() != null, "Date of birth must be set")
            .check(d -> d.getYearOfGraduation() > 1900, "Year of graduation must be valid")
            .check(d -> d.getNameOfClinic() != null && !d.getNameOfClinic().isEmpty(), "Clinic name must be set")
            .check(d -> d.getSpeciality() != null, "Speciality must be set")
            .get();
    }
}
