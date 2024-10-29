package dat.routes;

import dat.controllers.impl.DoctorControllerDB;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Routes configuration for the Medical Clinic API
 * Updated to use database-backed controller instead of mock
 */
public class Routes {
    // Replace mock controller with database controller
    private static final DoctorControllerDB doctorController = new DoctorControllerDB();

    public EndpointGroup getRoutes() {
        return () -> {
            path("doctors", () -> {
                // GET /api/doctors - Get all doctors
                get(doctorController::readAll);

                // POST /api/doctors - Create new doctor
                post(doctorController::create);

                // GET /api/doctors/{id} - Get specific doctor
                get("{id}", doctorController::read);

                // PUT /api/doctors/{id} - Update doctor
                put("{id}", doctorController::update);

                // GET /api/doctors/speciality/{speciality} - Get by speciality
                get("speciality/{speciality}",
                    doctorController::readBySpeciality);

                // GET /api/doctors/birthdate/range - Get by birthdate range
                get("birthdate/range",
                    doctorController::readByBirthdateRange);
            });
        };
    }
}