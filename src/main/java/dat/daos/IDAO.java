package dat.daos;

import dat.exceptions.ApiException;
import java.util.List;

/**
 * Generic DAO interface for basic CRUD operations
 * @param <T> The type of the entity/DTO
 * @param <ID> The type of the identifier
 */
public interface IDAO<T, ID> {
    // Basic CRUD operations
    T read(ID id);
    List<T> readAll();
    T create(T t) throws ApiException;
    T update(ID id, T t) throws ApiException;
    void delete(ID id);

    // Validation
    boolean validatePrimaryKey(ID id);
}