package srangeldev.camisapi.rest.users.services;

import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;

import java.util.List;

/**
 * Interfaz del servicio de usuarios
 */
public interface UserService {
    /**
     * Busca todos los usuarios.
     *
     * @return Lista de DTOs de respuesta de usuario.
     */
    List<UserResponseDto> findAll();

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario (Long).
     * @return DTO de respuesta del usuario.
     */
    UserResponseDto findById(Long id);

    /**
     * Busca usuarios por nombre.
     *
     * @param nombre Nombre a buscar
     * @return Lista de usuarios encontrados
     */
    List<UserResponseDto> findByNombre(String nombre);

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
     * @param id                   ID del usuario a actualizar (Long).
     * @param userUpdateRequestDto DTO con los datos a actualizar.
     * @return DTO de respuesta del usuario actualizado.
     */
    UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto);

    /**
     * Elimina un usuario por su ID (borrado físico).
     *
     * @param id ID del usuario a eliminar (Long).
     */
    void deleteById(Long id);
}
