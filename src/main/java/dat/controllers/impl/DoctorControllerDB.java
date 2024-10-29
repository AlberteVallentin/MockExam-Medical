package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import dat.exceptions.Message;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DoctorControllerDB implements IController<DoctorDTO, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorControllerDB.class);
    private final DoctorDAO dao;

    public DoctorControllerDB() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = DoctorDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        try {
            // Parse and validate ID
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

            // Check if doctor exists
            DoctorDTO doctor = dao.read(id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found with id: " + id);
            }

            // Return doctor if found
            ctx.status(200).json(doctor);

        } catch (ApiException e) {
            LOGGER.error("API Error in read: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in read", e);
            ctx.status(500)
                .json(new Message(500, "Internal server error: " + e.getMessage()));
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<DoctorDTO> doctors = dao.readAll();
            ctx.status(200).json(doctors);
        } catch (Exception e) {
            LOGGER.error("Error in readAll", e);
            ctx.status(500)
                .json(new Message(500, "Error fetching doctors: " + e.getMessage()));
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            // First try to parse the body to a DoctorDTO
            DoctorDTO doctorDTO;
            try {
                doctorDTO = ctx.bodyAsClass(DoctorDTO.class);
            } catch (Exception e) {
                throw new ApiException(400, "Invalid request body format");
            }

            // Then validate all required fields
            validateDoctorFields(doctorDTO);

            // Try to create the doctor
            DoctorDTO created = dao.create(doctorDTO);

            // Check if creation was successful
            if (created == null) {
                throw new ApiException(500, "Could not create doctor in database");
            }

            // Return successful response
            ctx.status(201).json(created);

        } catch (BadRequestResponse e) {
            LOGGER.error("Validation error in create: {}", e.getMessage());
            ctx.status(400)
                .json(new Message(400, "Validation error: " + e.getMessage()));
        } catch (ApiException e) {
            LOGGER.error("API Error in create: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in create", e);
            ctx.status(500)
                .json(new Message(500, "An unexpected error occurred while creating doctor"));
        }
    }

    private void validateDoctorFields(DoctorDTO doctor) throws ApiException {
        StringBuilder errors = new StringBuilder();

        if (doctor == null) {
            throw new ApiException(400, "Doctor data is required");
        }

        // Validate each field and collect all errors
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            errors.append("Name is required. ");
        }

        if (doctor.getDateOfBirth() == null) {
            errors.append("Date of birth is required. ");
        }

        if (doctor.getYearOfGraduation() <= 1900) {
            errors.append("Year of graduation must be after 1900. ");
        }

        if (doctor.getNameOfClinic() == null || doctor.getNameOfClinic().trim().isEmpty()) {
            errors.append("Clinic name is required. ");
        }

        if (doctor.getSpeciality() == null) {
            errors.append("Speciality is required. ");
        } else {
            try {
                Speciality.valueOf(doctor.getSpeciality().toString());
            } catch (IllegalArgumentException e) {
                errors.append("Invalid speciality value. Valid values are: ")
                    .append(String.join(", ", Arrays.stream(Speciality.values())
                        .map(Enum::name)
                        .toArray(String[]::new)))
                    .append(". ");
            }
        }

        // If any errors were found, throw exception with all error messages
        if (errors.length() > 0) {
            throw new ApiException(400, errors.toString().trim());
        }
    }

    @Override
    public DoctorDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(DoctorDTO.class)
            .check(d -> d.getName() != null && !d.getName().trim().isEmpty(), "Doctor name is required")
            .check(d -> d.getDateOfBirth() != null, "Date of birth is required")
            .check(d -> d.getYearOfGraduation() > 1900, "Year of graduation must be after 1900")
            .check(d -> d.getNameOfClinic() != null && !d.getNameOfClinic().trim().isEmpty(), "Clinic name is required")
            .check(d -> d.getSpeciality() != null, "Speciality is required")
            .get();
    }



    /**
     * Validates business rules for doctor creation/update
     * Throws ApiException if validation fails
     */
    private void validateBusinessRules(DoctorDTO doctor) throws ApiException {
        // Validate name
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new ApiException(400, "Doctor name is required");
        }

        // Validate date of birth
        if (doctor.getDateOfBirth() == null) {
            throw new ApiException(400, "Date of birth is required");
        }

        // Validate graduation year
        if (doctor.getYearOfGraduation() <= 1900) {
            throw new ApiException(400, "Year of graduation must be after 1900");
        }

        // Validate clinic name
        if (doctor.getNameOfClinic() == null || doctor.getNameOfClinic().trim().isEmpty()) {
            throw new ApiException(400, "Clinic name is required");
        }

        // Validate speciality
        if (doctor.getSpeciality() == null) {
            throw new ApiException(400, "Speciality is required");
        }
        try {
            Speciality.valueOf(doctor.getSpeciality().name());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid speciality value");
        }
    }


    @Override
    public void update(Context ctx) {
        try {
            // Parse and validate ID
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

            // Check if doctor exists
            if (!validatePrimaryKey(id)) {
                throw new ApiException(404, "Doctor not found with id: " + id);
            }

            // Update doctor
            DoctorDTO doctor = validateEntity(ctx);
            DoctorDTO updated = dao.update(id, doctor);
            ctx.status(200).json(updated);

        } catch (ApiException e) {
            LOGGER.error("API Error in update: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    public void readBySpeciality(Context ctx) {
        try {
            String specialityStr = ctx.pathParam("speciality").toUpperCase();
            Speciality speciality;
            try {
                speciality = Speciality.valueOf(specialityStr);
            } catch (IllegalArgumentException e) {
                throw new ApiException(400,
                    "Invalid speciality. Valid values are: " +
                        String.join(", ",
                            java.util.Arrays.stream(Speciality.values())
                                .map(Enum::name)
                                .toArray(String[]::new)));
            }

            List<DoctorDTO> doctors = dao.doctorBySpeciality(speciality);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found with speciality: " + speciality);
            }

            ctx.status(200).json(doctors);

        } catch (ApiException e) {
            LOGGER.error("API Error in readBySpeciality: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    public void readByBirthdateRange(Context ctx) {
        try {
            String fromStr = ctx.queryParam("from");
            String toStr = ctx.queryParam("to");

            if (fromStr == null || toStr == null) {
                throw new ApiException(400, "Both 'from' and 'to' dates are required");
            }

            LocalDate from, to;
            try {
                from = LocalDate.parse(fromStr);
                to = LocalDate.parse(toStr);
            } catch (Exception e) {
                throw new ApiException(400, "Invalid date format. Use: yyyy-MM-dd");
            }

            if (from.isAfter(to)) {
                throw new ApiException(400, "From date cannot be after to date");
            }

            List<DoctorDTO> doctors = dao.doctorByBirthdateRange(from, to);
            if (doctors.isEmpty()) {
                throw new ApiException(404,
                    String.format("No doctors found with birth dates between %s and %s", from, to));
            }

            ctx.status(200).json(doctors);

        } catch (ApiException e) {
            LOGGER.error("API Error in readByBirthdateRange: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }


    @Override
    public void delete(Context ctx) {
        try {
            // Parse and validate ID
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

            // Check if doctor exists before deletion
            if (!validatePrimaryKey(id)) {
                throw new ApiException(404, "Doctor not found with id: " + id);
            }

            // Perform deletion
            dao.delete(id);

            // Return 204 No Content on successful deletion
            ctx.status(204);

        } catch (ApiException e) {
            LOGGER.error("API Error in delete: {}", e.getMessage());
            ctx.status(e.getStatusCode())
                .json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in delete", e);
            ctx.status(500)
                .json(new Message(500, "Internal server error while deleting doctor: " + e.getMessage()));
        }
    }
}
