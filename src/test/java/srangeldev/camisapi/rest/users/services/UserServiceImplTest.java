package srangeldev.camisapi.rest.users.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserBadRequest;
import srangeldev.camisapi.rest.users.exceptions.UserNotFound;
import srangeldev.camisapi.rest.users.mappers.UserMapper;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // Datos de prueba
    private User user;
    private UserResponseDto userResponseDto;
    private UserCreateRequestDto userCreateRequestDto;
    private UserUpdateRequestDto userUpdateRequestDto;
    private ObjectId userId;

    @BeforeEach
    void setUp() {
        userId = new ObjectId();
        LocalDateTime now = LocalDateTime.now();

        user = User.builder()
                .id(userId)
                .nombre("Test User")
                .username("testuser")
                .password("hashedPassword")
                .roles(Set.of(Rol.USER))
                .createdAt(now)
                .updatedAt(now)
                .build();

        userResponseDto = UserResponseDto.builder()
                .id(userId.toHexString())
                .nombre("Test User")
                .username("testuser")
                .roles(Set.of(Rol.USER))
                .createdAt(now)
                .updatedAt(now)
                .build();

        userCreateRequestDto = UserCreateRequestDto.builder()
                .nombre("New User")
                .username("newuser")
                .password("password123")
                .roles(Set.of(Rol.USER))
                .build();

        userUpdateRequestDto = UserUpdateRequestDto.builder()
                .nombre("Updated User")
                .username("updateduser")
                .roles(Set.of(Rol.ADMIN))
                .build();
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Debe devolver todos los usuarios")
        void findAll_ShouldReturnAllUsers() {
            // Arrange
            when(userRepository.findAll()).thenReturn(List.of(user));
            when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

            // Act
            List<UserResponseDto> result = userService.findAll();

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals(userResponseDto, result.get(0)),
                    () -> verify(userRepository, times(1)).findAll(),
                    () -> verify(userMapper, times(1)).toUsuarioResponseDto(user)
            );
        }

        @Test
        @DisplayName("Debe devolver lista vacía si no hay usuarios")
        void findAll_ShouldReturnEmptyList() {
            // Arrange
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<UserResponseDto> result = userService.findAll();

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertTrue(result.isEmpty()),
                    () -> verify(userRepository, times(1)).findAll(),
                    () -> verify(userMapper, never()).toUsuarioResponseDto(any())
            );
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Debe devolver usuario por ID")
        void findById_ShouldReturnUser() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userMapper.toUsuarioResponseDto(user)).thenReturn(userResponseDto);

            // Act
            UserResponseDto result = userService.findById(userId);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userResponseDto, result),
                    () -> verify(userRepository, times(1)).findById(userId),
                    () -> verify(userMapper, times(1)).toUsuarioResponseDto(user)
            );
        }

        @Test
        @DisplayName("Debe lanzar UserNotFound si ID no existe")
        void findById_ShouldThrowUserNotFound() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            var exception = assertThrows(UserNotFound.class, () -> {
                userService.findById(userId);
            });

            assertEquals("Usuario con username Usuario con id " + userId + " no encontrado no encontrado", exception.getMessage());
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, never()).toUsuarioResponseDto(any());
        }
    }

    @Nested
    @DisplayName("FindByNombre Tests")
    class FindByNombreTests {

        @Test
        @DisplayName("Debe devolver usuarios por nombre")
        void findByNombre_ShouldReturnUsers() {
            // Arrange
            when(userRepository.findByNombre("Test User")).thenReturn(List.of(user));
            when(userMapper.toUsuarioResponseDto(user)).thenReturn(userResponseDto);

            // Act
            List<UserResponseDto> result = userService.findByNombre("Test User");

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.size()),
                    () -> verify(userRepository, times(1)).findByNombre("Test User"),
                    () -> verify(userMapper, times(1)).toUsuarioResponseDto(user)
            );
        }
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar un nuevo usuario")
        void save_ShouldSaveUser() {
            // Arrange
            User userToSave = User.builder() // Simula lo que devuelve el mapper
                    .nombre(userCreateRequestDto.getNombre())
                    .username(userCreateRequestDto.getUsername())
                    .password(userCreateRequestDto.getPassword())
                    .roles(userCreateRequestDto.getRoles())
                    .build();

            User savedUser = User.builder() // Simula lo que devuelve el repo.save()
                    .id(new ObjectId())
                    .nombre(userToSave.getNombre())
                    .username(userToSave.getUsername())
                    .password(userToSave.getPassword())
                    .roles(userToSave.getRoles())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            when(userRepository.findByUsername(userCreateRequestDto.getUsername())).thenReturn(Optional.empty());
            when(userMapper.toUsuario(userCreateRequestDto)).thenReturn(userToSave);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(userMapper.toUsuarioResponseDto(savedUser)).thenReturn(userResponseDto);

            // Usar ArgumentCaptor para verificar el objeto ANTES de guardarlo
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            // Act
            UserResponseDto result = userService.save(userCreateRequestDto);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userResponseDto, result),
                    () -> verify(userRepository, times(1)).findByUsername(userCreateRequestDto.getUsername()),
                    () -> verify(userMapper, times(1)).toUsuario(userCreateRequestDto),
                    () -> verify(userRepository, times(1)).save(userCaptor.capture()),
                    () -> verify(userMapper, times(1)).toUsuarioResponseDto(savedUser),
                    // Verificar que el servicio asignó ID y fechas
                    () -> assertNotNull(userCaptor.getValue().getId()),
                    () -> assertNotNull(userCaptor.getValue().getCreatedAt()),
                    () -> assertNotNull(userCaptor.getValue().getUpdatedAt())
            );
        }

        @Test
        @DisplayName("Debe lanzar UserBadRequest si el username ya existe")
        void save_ShouldThrowUserBadRequestOnDuplicateUsername() {
            // Arrange
            when(userRepository.findByUsername(userCreateRequestDto.getUsername())).thenReturn(Optional.of(user));

            // Act & Assert
            var exception = assertThrows(UserBadRequest.class, () -> {
                userService.save(userCreateRequestDto);
            });

            assertEquals("Ya existe un usuario con el username " + userCreateRequestDto.getUsername(), exception.getMessage());
            verify(userRepository, times(1)).findByUsername(userCreateRequestDto.getUsername());
            verify(userMapper, never()).toUsuario(any());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar un usuario completamente")
        void update_ShouldUpdateUserFully() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(userUpdateRequestDto.getUsername())).thenReturn(Optional.empty());
            when(userRepository.save(any(User.class))).thenReturn(user); // Devuelve el usuario modificado
            when(userMapper.toUsuarioResponseDto(user)).thenReturn(userResponseDto);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            // Act
            UserResponseDto result = userService.update(userId, userUpdateRequestDto);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userResponseDto, result),
                    () -> verify(userRepository, times(1)).findById(userId),
                    () -> verify(userRepository, times(1)).findByUsername(userUpdateRequestDto.getUsername()),
                    () -> verify(userRepository, times(1)).save(userCaptor.capture()),
                    () -> verify(userMapper, times(1)).toUsuarioResponseDto(user),
                    // Verificar que los campos se actualizaron ANTES de guardar
                    () -> assertEquals(userUpdateRequestDto.getNombre(), userCaptor.getValue().getNombre()),
                    () -> assertEquals(userUpdateRequestDto.getUsername(), userCaptor.getValue().getUsername()),
                    () -> assertEquals(userUpdateRequestDto.getRoles(), userCaptor.getValue().getRoles())
            );
        }

        @Test
        @DisplayName("Debe lanzar UserNotFound si el usuario a actualizar no existe")
        void update_ShouldThrowUserNotFound() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFound.class, () -> {
                userService.update(userId, userUpdateRequestDto);
            });

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).findByUsername(anyString());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar UserBadRequest si el nuevo username ya existe")
        void update_ShouldThrowUserBadRequestOnDuplicateUsername() {
            // Arrange
            User existingUserWithSameUsername = User.builder().id(new ObjectId()).username("updateduser").build();
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(userUpdateRequestDto.getUsername())).thenReturn(Optional.of(existingUserWithSameUsername));

            // Act & Assert
            assertThrows(UserBadRequest.class, () -> {
                userService.update(userId, userUpdateRequestDto);
            });

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).findByUsername(userUpdateRequestDto.getUsername());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe saltar la validación de username si es nulo")
        void update_ShouldSkipUsernameValidationIfNull() {
            // Arrange
            userUpdateRequestDto.setUsername(null);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            userService.update(userId, userUpdateRequestDto);

            // Assert
            verify(userRepository, times(1)).findById(userId);
            // No debe llamar a findByUsername si el DTO trae null
            verify(userRepository, never()).findByUsername(anyString());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Debe saltar la validación de username si está vacío")
        void update_ShouldSkipUsernameValidationIfEmpty() {
            // Arrange
            userUpdateRequestDto.setUsername("");
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            userService.update(userId, userUpdateRequestDto);

            // Assert
            verify(userRepository, never()).findByUsername(anyString());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Debe saltar la validación de username si es el mismo")
        void update_ShouldSkipUsernameValidationIfSame() {
            // Arrange
            userUpdateRequestDto.setUsername(user.getUsername()); // Mismo username
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            userService.update(userId, userUpdateRequestDto);

            // Assert
            verify(userRepository, never()).findByUsername(anyString());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Debe ignorar campos nulos o vacíos en la actualización")
        void update_ShouldIgnoreNullAndEmptyFields() {
            // Arrange
            UserUpdateRequestDto partialUpdateDto = UserUpdateRequestDto.builder()
                    .nombre(null)
                    .username(null)
                    .roles(Collections.emptySet())
                    .build();

            String originalNombre = user.getNombre();
            Set<Rol> originalRoles = user.getRoles();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            // Act
            userService.update(userId, partialUpdateDto);

            // Assert
            verify(userRepository, times(1)).save(userCaptor.capture());
            // Los campos que eran nulos/vacíos en el DTO deben retener el valor original
            assertEquals(originalNombre, userCaptor.getValue().getNombre());
            assertEquals(originalRoles, userCaptor.getValue().getRoles());
        }
    }

    @Nested
    @DisplayName("DeleteById Tests")
    class DeleteByIdTests {

        @Test
        @DisplayName("Debe eliminar un usuario")
        void deleteById_ShouldDeleteUser() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            doNothing().when(userRepository).deleteById(userId); // void method

            // Act
            userService.deleteById(userId);

            // Assert
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("Debe lanzar UserNotFound si el usuario a eliminar no existe")
        void deleteById_ShouldThrowUserNotFound() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFound.class, () -> {
                userService.deleteById(userId);
            });

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).deleteById(any());
        }
    }
}