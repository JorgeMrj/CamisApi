package srangeldev.camisapi.rest.users.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.users.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para User - MongoDB
 * 
 * Utiliza MongoRepository para acceder a los usuarios almacenados en MongoDB.
 * Es el único repositorio que usa MongoDB según la arquitectura híbrida.
 * 
 * MongoRepository proporciona métodos básicos CRUD y permite definir queries personalizadas
 * usando @Query con sintaxis de MongoDB o mediante la nomenclatura de Spring Data.
 */
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    /**
     * Busca un usuario por su nombre de usuario (username)
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca usuarios por nombre usando Query con sintaxis MongoDB.
     * Ejemplo de consulta usando @Query con sintaxis de MongoDB.
     * $regex permite búsqueda por patrones y $options: 'i' hace la búsqueda case-insensitive.
     *
     * @param nombre Nombre del usuario
     * @return Lista de usuarios que coinciden con el patrón
     */
    @Query("{ 'nombre' : { $regex: ?0, $options: 'i' } }")
    List<User> findByNombre(String nombre);

    /**
     * Busca usuarios por username (like) usando regex.
     * Utilizamos regex para búsqueda por patrones en MongoDB.
     *
     * @param username Username a buscar
     * @return Lista de usuarios
     */
    @Query("{'username': {$regex: ?0, $options: 'i'}}")
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * Busca usuarios por estado de borrado.
     * Ejemplo de Query Derivation simple.
     *
     * @param isDeleted true para buscar borrados, false para activos
     * @return Lista de usuarios
     */
    List<User> findByIsDeleted(Boolean isDeleted);

    /**
     * Busca usuarios por username (like) y estado de borrado.
     * Utilizamos regex para búsqueda por patrones en MongoDB.
     *
     * @param username  Username a buscar
     * @param isDeleted Estado de borrado
     * @return Lista de usuarios
     */
    @Query("{'username': {$regex: ?0, $options: 'i'}, 'isDeleted': ?1}")
    List<User> findByUsernameContainingIgnoreCaseAndIsDeleted(String username, Boolean isDeleted);
}
