package srangeldev.camisapi.rest.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.users.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     * Es insensible a mayúsculas/minúsculas.
     *
     * @param username Nombre de usuario
     * @return Optional<User> con el usuario si se encuentra
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Busca un usuario por su email.
     * Es insensible a mayúsculas/minúsculas.
     *
     * @param email Email del usuario
     * @return Optional<User> con el usuario si se encuentra
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Busca un usuario por su nombre de usuario o email.
     * Es insensible a mayúsculas/minúsculas.
     *
     * @param username Nombre de usuario
     * @param email    Email
     * @return Optional<User> con el usuario si se encuentra
     */
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    /**
     * Busca usuarios por estado de borrado.
     *
     * @param isDeleted true para buscar borrados, false para activos
     * @return Lista de usuarios
     */
    List<User> findByIsDeleted(Boolean isDeleted);

    /**
     * Busca usuarios por username (like) y estado de borrado.
     *
     * @param username  Username a buscar
     * @param isDeleted Estado de borrado
     * @return Lista de usuarios
     */
    List<User> findByUsernameContainingIgnoreCaseAndIsDeleted(String username, Boolean isDeleted);

    /**
     * Busca usuarios por username (like).
     *
     * @param username Username a buscar
     * @return Lista de usuarios
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * Actualiza el estado de borrado lógico de un usuario.
     *
     * @param id        ID del usuario
     * @param isDeleted true si se quiere marcar como borrado, false en caso contrario
     */
    @Modifying
    @Query("UPDATE User u SET u.isDeleted = :isDeleted WHERE u.id = :id")
    void updateIsDeletedById(Long id, Boolean isDeleted);
}
