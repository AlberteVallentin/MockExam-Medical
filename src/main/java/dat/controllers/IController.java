package dat.controllers;

import io.javalin.http.Context;

/**
 * Generic interface for REST controllers
 * Defines basic CRUD operations and validation methods
 * @param <T> The type of the DTO
 * @param <ID> The type of the identifier
 */
public interface IController<T, ID> {
    /**
     * Retrieves a single entity by its ID
     * @param ctx Javalin context containing the ID parameter
     */
    void read(Context ctx);

    /**
     * Retrieves all entities
     * @param ctx Javalin context for returning the response
     */
    void readAll(Context ctx);

    /**
     * Creates a new entity
     * @param ctx Javalin context containing the entity data in request body
     */
    void create(Context ctx);

    /**
     * Updates an existing entity
     * @param ctx Javalin context containing the ID parameter and updated entity data
     */
    void update(Context ctx);

    /**
     * Deletes an entity by its ID
     * @param ctx Javalin context containing the ID parameter
     */
    void delete(Context ctx);

    /**
     * Validates if a given ID exists in the system
     * @param id The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    boolean validatePrimaryKey(ID id);

    /**
     * Validates entity data from request body
     * Implementations should perform all necessary validation checks
     * @param ctx Javalin context containing the entity data to validate
     * @return Validated entity object
     */
    T validateEntity(Context ctx);
}