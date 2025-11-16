package srangeldev.camisapi.rest.users.mappers;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.rest.users.models.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;
    private UserCreateRequestDto createDto;
    private UserUpdateRequestDto updateDto;
    private User user;
    private ObjectId userId;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(); // Instanciamos el mapper
        userId = new ObjectId();
        LocalDateTime now = LocalDateTime.now();

        // DTO para crear
        createDto = UserCreateRequestDto.builder()
                .nombre("Test User")
                .username("testuser")
                .password("password123")
                .roles(Set.of(Rol.USER))
                .build();

        // Entidad completa
        user = User.builder()
                .id(userId)
                .nombre("Original Name")
                .username("originaluser")
                .password("hashedPassword")
                .roles(Set.of(Rol.USER))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Nested
    @DisplayName("toUsuario (from UserCreateRequestDto)")
    class ToUserFromCreateDto {

        @Test
        @DisplayName("Debe mapear CreateDto a User correctamente")
        void toUsuario_fromCreateDto_ShouldMapCorrectly() {
            // Act
            User result = userMapper.toUsuario(createDto);

            // Assert
            assertAll(
                    () -> assertEquals(createDto.getNombre(), result.getNombre()),
                    () -> assertEquals(createDto.getUsername(), result.getUsername()),
                    () -> assertEquals(createDto.getPassword(), result.getPassword()),
                    () -> assertEquals(createDto.getRoles(), result.getRoles()),
                    // Campos no asignados por este mapper deben ser nulos
                    () -> assertNull(result.getId())
            );
        }
    }

    @Nested
    @DisplayName("toUsuarioResponseDto (from User)")
    class ToUserResponseDto {

        @Test
        @DisplayName("Debe mapear User a ResponseDto correctamente")
        void toUsuarioResponseDto_ShouldMapCorrectly() {
            // Act
            UserResponseDto result = userMapper.toUsuarioResponseDto(user);

            // Assert
            assertAll(
                    // Convierte ObjectId a String
                    () -> assertEquals(user.getId().toHexString(), result.getId()),
                    () -> assertEquals(user.getNombre(), result.getNombre()),
                    () -> assertEquals(user.getUsername(), result.getUsername()),
                    () -> assertEquals(user.getRoles(), result.getRoles()),
                    () -> assertEquals(user.getCreatedAt(), result.getCreatedAt()),
                    () -> assertEquals(user.getUpdatedAt(), result.getUpdatedAt())
            );
        }

        @Test
        @DisplayName("Debe manejar ID nulo (para usuarios aún no guardados)")
        void toUsuarioResponseDto_ShouldHandleNullId() {
            // Arrange
            user.setId(null);

            // Act
            UserResponseDto result = userMapper.toUsuarioResponseDto(user);

            // Assert
            assertNull(result.getId());
            assertEquals(user.getNombre(), result.getNombre());
        }
    }

    @Nested
    @DisplayName("toUsuario (from UserUpdateRequestDto)")
    class ToUserFromUpdateDto {

        @Test
        @DisplayName("Debe actualizar todos los campos si no son nulos")
        void toUsuario_fromUpdateDto_ShouldUpdateAllFields() {
            // Arrange
            updateDto = UserUpdateRequestDto.builder()
                    .nombre("Updated Name")
                    .username("updateduser")
                    .roles(Set.of(Rol.ADMIN))
                    .build();

            // Act
            User result = userMapper.toUsuario(updateDto, user);

            // Assert
            assertAll(
                    () -> assertSame(user, result, "Debe retornar la misma instancia de User"),
                    () -> assertEquals("Updated Name", result.getNombre()),
                    () -> assertEquals("updateduser", result.getUsername()),
                    () -> assertEquals(Set.of(Rol.ADMIN), result.getRoles()),
                    // La contraseña no debe cambiar
                    () -> assertEquals("hashedPassword", result.getPassword())
            );
        }

        @Test
        @DisplayName("Debe ignorar campos nulos y mantener valores originales")
        void toUsuario_fromUpdateDto_ShouldIgnoreNullFields() {
            // Arrange: DTO solo con nombre
            updateDto = UserUpdateRequestDto.builder()
                    .nombre("Updated Name")
                    .username(null) // Username nulo
                    .roles(null)    // Roles nulos
                    .build();

            // Act
            User result = userMapper.toUsuario(updateDto, user);

            // Assert
            assertAll(
                    () -> assertEquals("Updated Name", result.getNombre()), // Campo actualizado
                    // Campos originales mantenidos
                    () -> assertEquals("originaluser", result.getUsername()),
                    () -> assertEquals(Set.of(Rol.USER), result.getRoles())
            );
        }

        @Test
        @DisplayName("Debe ignorar roles si el Set está vacío")
        void toUsuario_fromUpdateDto_ShouldIgnoreEmptyRoles() {
            // Arrange: DTO con roles vacíos
            updateDto = UserUpdateRequestDto.builder()
                    .roles(Collections.emptySet())
                    .build();

            // Act
            User result = userMapper.toUsuario(updateDto, user);

            // Assert: Los roles originales deben mantenerse
            assertEquals(Set.of(Rol.USER), result.getRoles());
        }

        @Test
        @DisplayName("No debe actualizar nada si todos los campos del DTO son nulos")
        void toUsuario_fromUpdateDto_ShouldDoNothingIfAllNull() {
            // Arrange
            updateDto = UserUpdateRequestDto.builder()
                    .nombre(null)
                    .username(null)
                    .roles(null)
                    .build();

            // Guardamos una copia del estado original
            String originalName = user.getNombre();
            String originalUsername = user.getUsername();
            Set<Rol> originalRoles = user.getRoles();

            // Act
            User result = userMapper.toUsuario(updateDto, user);

            // Assert
            assertAll(
                    () -> assertEquals(originalName, result.getNombre()),
                    () -> assertEquals(originalUsername, result.getUsername()),
                    () -> assertEquals(originalRoles, result.getRoles())
            );
        }
    }
}