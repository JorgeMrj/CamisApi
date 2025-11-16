package srangeldev.camisapi.rest.users.mappers;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.models.User;

@Component
public class UserMapper {

    /**
     * Convierte un DTO de creación a una entidad User.
     * La contraseña se deja en texto plano para que el servicio la codifique.
     * Los campos como createdAt, updatedAt, etc., se dejan con sus valores
     * por defecto de la entidad.
     */
    public User toUsuario(UserCreateRequestDto createDto) {
        return User.builder()
                .nombre(createDto.getNombre())
                .username(createDto.getUsername())
                .password(createDto.getPassword())
                .roles(createDto.getRoles())
                .build();
    }

    /**
     * Convierte una entidad User a un DTO de respuesta.
     * IMPORTANTE: Omite la contraseña y convierte ObjectId a String.
     */
    public UserResponseDto toUsuarioResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId() != null ? user.getId().toHexString() : null)
                .nombre(user.getNombre())
                .username(user.getUsername())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Actualiza una entidad User existente con los datos de un DTO de actualización.
     * Solo actualiza los campos que no son nulos en el DTO, permitiendo
     * actualizaciones parciales.
     *
     * @param updateDto El DTO con los campos a actualizar.
     * @param user   La entidad original a modificar.
     * @return La misma entidad 'user' modificada.
     */
    public User toUsuario(UserUpdateRequestDto updateDto, User user) {
        if (updateDto.getNombre() != null) {
            user.setNombre(updateDto.getNombre());
        }
        if (updateDto.getUsername() != null) {
            user.setUsername(updateDto.getUsername());
        }
        if (updateDto.getRoles() != null && !updateDto.getRoles().isEmpty()) {
            user.setRoles(updateDto.getRoles());
        }
        // La contraseña no se actualiza aquí
        return user;
    }
}