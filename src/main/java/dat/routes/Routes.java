package dat.routes;

import dat.controllers.impl.DoctorMockController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Routes configuration for the Medical Clinic API
 * Task 1.5.1 - Implementation of routing configuration
 */
public class Routes {
    private static final DoctorMockController doctorController = new DoctorMockController();

    public EndpointGroup getRoutes() {
        return () -> {
            path("doctors", () -> {
                get(doctorController::readAll);                           // GET /api/doctors
                post(doctorController::create);                          // POST /api/doctors
                get("{id}", doctorController::read);                     // GET /api/doctors/{id}
                put("{id}", doctorController::update);                   // PUT /api/doctors/{id}
                get("speciality/{speciality}",
                    doctorController::readBySpeciality);                // GET /api/doctors/speciality/{speciality}
                get("birthdate/range",
                    doctorController::readByBirthdateRange);            // GET /api/doctors/birthdate/range
            });
        };
    }
}