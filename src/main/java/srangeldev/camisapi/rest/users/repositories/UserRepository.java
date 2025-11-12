package srangeldev.camisapi.rest.users.repositories;

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
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     * MongoDB es sensible a mayúsculas/minúsculas por defecto, 
     * por lo que usamos regex para hacer búsqueda insensible.
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si se encuentra
     */
    @Query("{'username': {$regex: ?0, $options: 'i'}}")
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Busca usuarios por estado de borrado.
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

    /**
     * Busca usuarios por username (like).
     * Utilizamos regex para búsqueda por patrones en MongoDB.
     *
     * @param username Username a buscar
     * @return Lista de usuarios
     */
    @Query("{'username': {$regex: ?0, $options: 'i'}}")
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * Actualiza el estado de borrado lógico de un usuario.
     * En MongoDB, usamos el método save() después de modificar el objeto
     * o implementamos este método personalizado.
     *
     * @param id        ID del usuario
     * @param isDeleted true si se quiere marcar como borrado, false en caso contrario
     */
    @Query("{'_id': ?0}")
    Optional<User> findByIdForUpdate(String id);
    
    // Nota: Para operaciones de actualización en MongoDB, es preferible
    // cargar el objeto, modificarlo y guardarlo usando save()
    // En lugar de usar @Modifying, implementamos la lógica en el servicio
}
