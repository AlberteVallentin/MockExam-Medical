package dat.controllers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller handling all doctor-related HTTP endpoints
 * Implements basic CRUD operations and specialized doctor queries
 */
public class DoctorMockController implements IController<DoctorDTO, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorMockController.class);
    private final DoctorMockDAO dao;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    // Initialize controller with DAO instance
    public DoctorMockController() {
        this.dao = new DoctorMockDAO();
    }

    /**
     * Retrieves a specific doctor by their ID
     *
     * @param ctx Javalin context containing the ID parameter
     * @throws ApiException with 404 if doctor not found
     */
    @Override
    public void read(Context ctx) {
        try {
            // Parse and validate ID with better error handling
            String idParam = ctx.pathParam("id");
            int id;

            try {
                id = Integer.parseInt(idParam);
                if (id <= 0) {
                    throw new ApiException(400, "ID must be a positive number");
                }
            } catch (NumberFormatException e) {
                throw new ApiException(400, "Invalid ID format: " + idParam);
            }

            DoctorDTO doctorDTO = dao.read(id);
            if (doctorDTO == null) {
                throw new ApiException(404, "Doctor not found with id: " + id);
            }

            ctx.json(doctorDTO);
        } catch (ApiException e) {
            LOGGER.error("API Error in read: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in read", e);
            ctx.status(500);
            ctx.json(new Message(500, "Internal server error"));
        }
    }

    /**
     * Retrieves all doctors in the system
     *
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
     *
     * @param ctx Javalin context containing the doctor data in request body
     */
    /**
     * Creates a new doctor
     * @param ctx Javalin context containing the doctor data in request body
     */
    @Override
    public void create(Context ctx) {
        try {
            // Try to parse the request body to DoctorDTO
            DoctorDTO newDoctor;
            try {
                newDoctor = ctx.bodyAsClass(DoctorDTO.class);
            } catch (Exception e) {
                // Handle any parsing errors (including invalid date format)
                throw new ApiException(400, "Invalid input format. Please check your data format. Dates should be in yyyy-MM-dd format");
            }

            // Validate the parsed doctor data
            if (newDoctor.getName() == null || newDoctor.getName().trim().isEmpty()) {
                throw new ApiException(400, "Doctor name is required");
            }
            if (newDoctor.getDateOfBirth() == null) {
                throw new ApiException(400, "Date of birth is required");
            }
            if (newDoctor.getYearOfGraduation() <= 1900) {
                throw new ApiException(400, "Invalid graduation year");
            }
            if (newDoctor.getNameOfClinic() == null || newDoctor.getNameOfClinic().trim().isEmpty()) {
                throw new ApiException(400, "Clinic name is required");
            }
            if (newDoctor.getSpeciality() == null) {
                throw new ApiException(400, "Speciality is required");
            }

            // Create the doctor if validation passes
            DoctorDTO createdDoctor = dao.create(newDoctor);

            // Return 201 Created on success
            ctx.status(201);
            ctx.json(createdDoctor);

        } catch (ApiException e) {
            // Handle our custom API exceptions with 400 status
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }
    /**
     * Updates an existing doctor's information
     *
     * @param ctx Javalin context containing the ID parameter and updated doctor data
     */
    @Override
    public void update(Context ctx) {
        try {
            // Extract and validate ID
            int id = ctx.pathParamAsClass("id", Integer.class)
                .get();

            // Check if doctor exists
            if (!validatePrimaryKey(id)) {
                throw new ApiException(404, "Doctor with ID " + id + " not found");
            }

            // Validate update data
            DoctorDTO updatedDoctor = validateEntity(ctx);

            // Perform business validation
            validateDoctorData(updatedDoctor);

            // Attempt update
            DoctorDTO result = dao.update(id, updatedDoctor);

            if (result == null) {
                throw new ApiException(404, "Doctor with ID " + id + " could not be updated");
            }

            // Return successful response
            ctx.status(200);
            ctx.json(result);

        } catch (ApiException e) {
            LOGGER.error("API Error in update: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in update", e);
            ctx.status(500);
            ctx.json(new Message(500, "An unexpected error occurred while updating the doctor"));
        }
    }

    /**
     * Deletes a doctor by their ID
     *
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
     *
     * @param ctx Javalin context containing the speciality parameter
     * @throws ApiException with 404 if no doctors found with given speciality
     */
    public void readBySpeciality(Context ctx) {
        try {
            String specialityStr = ctx.pathParam("speciality").toUpperCase();
            Speciality speciality;

            try {
                speciality = Speciality.valueOf(specialityStr);
            } catch (IllegalArgumentException e) {
                String validSpecialities = Arrays.toString(Speciality.values());
                throw new ApiException(400, "Invalid speciality. Valid values are: " + validSpecialities);
            }

            List<DoctorDTO> doctors = dao.doctorBySpeciality(speciality);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found with speciality: " + speciality);
            }

            ctx.json(doctors);
        } catch (ApiException e) {
            LOGGER.error("API Error in readBySpeciality: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in readBySpeciality", e);
            ctx.status(500);
            ctx.json(new Message(500, "Internal server error"));
        }
    }


    /**
     * Finds doctors born within a specific date range
     *
     * @param ctx Javalin context containing the date range parameters
     * @throws ApiException with 400 if date range is invalid
     * @throws ApiException with 404 if no doctors found in range
     */
    public void readByBirthdateRange(Context ctx) {
        try {
            String fromStr = ctx.queryParam("from");
            String toStr = ctx.queryParam("to");

            if (fromStr == null || toStr == null) {
                throw new ApiException(400, "Both 'from' and 'to' dates are required");
            }

            LocalDate fromDate;
            LocalDate toDate;
            try {
                fromDate = LocalDate.parse(fromStr);
                toDate = LocalDate.parse(toStr);
            } catch (DateTimeParseException e) {
                throw new ApiException(400, "Invalid date format. Use yyyy-MM-dd");
            }

            if (fromDate.isAfter(toDate)) {
                throw new ApiException(400, "From date cannot be after to date");
            }

            List<DoctorDTO> doctors = dao.doctorByBirthdateRange(fromDate, toDate);
            if (doctors.isEmpty()) {
                throw new ApiException(404, String.format("No doctors found with birth dates between %s and %s", fromDate, toDate));
            }

            ctx.json(doctors);
        } catch (ApiException e) {
            LOGGER.error("API Error in readByBirthdateRange: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in readByBirthdateRange", e);
            ctx.status(500);
            ctx.json(new Message(500, "Internal server error"));
        }
    }

    /**
     * Validates doctor entity data
     * Ensures all required fields are present and valid
     */
    private void validateDoctorData(DoctorDTO doctor) throws ApiException {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new ApiException(400, "Doctor name is required");
        }
        if (doctor.getDateOfBirth() == null) {
            throw new ApiException(400, "Date of birth is required");
        }
        // Add date format validation
        try {
            String dateStr = doctor.getDateOfBirth().toString();
            if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new ApiException(400, "Invalid date format. Please use format: yyyy-MM-dd");
            }
        } catch (Exception e) {
            throw new ApiException(400, "Invalid date format. Please use format: yyyy-MM-dd");
        }
        if (doctor.getYearOfGraduation() <= 1900) {
            throw new ApiException(400, "Invalid graduation year");
        }
        if (doctor.getNameOfClinic() == null || doctor.getNameOfClinic().trim().isEmpty()) {
            throw new ApiException(400, "Clinic name is required");
        }
        if (doctor.getSpeciality() == null) {
            throw new ApiException(400, "Speciality is required");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

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