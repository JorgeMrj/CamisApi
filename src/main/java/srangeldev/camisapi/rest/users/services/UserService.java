package srangeldev.camisapi.rest.users.services;

import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Busca todos los usuarios.
     *
     * @param username  Opcional: filtra por nombre de usuario.
     * @param email     Opcional: filtra por email.
     * @param isDeleted Opcional: filtra por estado de borrado.
     * @return Lista de DTOs de respuesta de usuario.
     */
    List<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted);

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return DTO de respuesta del usuario.
     */
    UserResponseDto findById(Long id);

    /**
     * Guarda un nuevo usuario.
     *
     * @param userCreateRequestDto DTO con los datos del nuevo usuario.
     * @return DTO de respuesta del usuario creado.
     */
    UserResponseDto save(UserCreateRequestDto userCreateRequestDto);

    /**
     * Actualiza un usuario existente.
     *
     * @param id                   ID del usuario a actualizar.
     * @param userUpdateRequestDto DTO con los datos a actualizar.
     * @return DTO de respuesta del usuario actualizado.
     */
    UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto);

    /**
     * Elimina un usuario por su ID (borrado lógico).
     *
     * @param id ID del usuario a eliminar.
     */
    void deleteById(Long id);
}
