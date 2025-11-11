package srangeldev.camisapi.rest.users.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserBadRequest;
import srangeldev.camisapi.rest.users.mappers.UserMapper;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    private User user;
    private UserResponseDto userResponseDto;
    private UserCreateRequestDto userCreateRequestDto;
    private UserUpdateRequestDto userUpdateRequestDto;

    @BeforeEach
    void setUp() {
        // User entity
        user = User.builder()
                .id(1L)
                .nombre("Test User")
                .username("testuser")
                .password("password123")
                .roles(Set.of(Rol.USER))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        // UserResponseDto
        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .nombre("Test User")
                .username("testuser")
                .roles(Set.of(Rol.USER))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // UserCreateRequestDto
        userCreateRequestDto = UserCreateRequestDto.builder()
                .nombre("New User")
                .username("newuser")
                .password("password123")
                .roles(Set.of(Rol.USER))
                .build();

        // UserUpdateRequestDto
        userUpdateRequestDto = UserUpdateRequestDto.builder()
                .nombre("Updated User")
                .username("updateduser")
                .roles(Set.of(Rol.ADMIN))
                .build();
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe devolver todos los usuarios sin filtros")
            void findAll_SinFiltros_DevuelveTodosLosUsuarios() {
                // Arrange
                List<User> users = Arrays.asList(user);
                when(userRepository.findAll()).thenReturn(users);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                List<UserResponseDto> result = userService.findAll(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                );

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                verify(userRepository).findAll();
                verify(userMapper, times(1)).toUsuarioResponseDto(any(User.class));
            }

            @Test
            @DisplayName("Debe filtrar por username")
            void findAll_ConUsername_DevuelveUsuariosFiltrados() {
                // Arrange
                List<User> users = Arrays.asList(user);
                when(userRepository.findByUsernameContainingIgnoreCase(anyString())).thenReturn(users);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                List<UserResponseDto> result = userService.findAll(
                        Optional.of("test"),
                        Optional.empty(),
                        Optional.empty()
                );

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                verify(userRepository).findByUsernameContainingIgnoreCase("test");
                verify(userMapper, times(1)).toUsuarioResponseDto(any(User.class));
            }

            @Test
            @DisplayName("Debe filtrar por isDeleted")
            void findAll_ConIsDeleted_DevuelveUsuariosFiltrados() {
                // Arrange
                List<User> users = Arrays.asList(user);
                when(userRepository.findByIsDeleted(anyBoolean())).thenReturn(users);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                List<UserResponseDto> result = userService.findAll(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(false)
                );

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                verify(userRepository).findByIsDeleted(false);
                verify(userMapper, times(1)).toUsuarioResponseDto(any(User.class));
            }

            @Test
            @DisplayName("Debe filtrar por username y isDeleted")
            void findAll_ConUsernameYIsDeleted_DevuelveUsuariosFiltrados() {
                // Arrange
                List<User> users = Arrays.asList(user);
                when(userRepository.findByUsernameContainingIgnoreCaseAndIsDeleted(anyString(), anyBoolean())).thenReturn(users);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                List<UserResponseDto> result = userService.findAll(
                        Optional.of("test"),
                        Optional.empty(),
                        Optional.of(false)
                );

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                verify(userRepository).findByUsernameContainingIgnoreCaseAndIsDeleted("test", false);
                verify(userMapper, times(1)).toUsuarioResponseDto(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe devolver un usuario por ID")
            void findById_ConIdExistente_DevuelveUsuario() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.findById(1L);

                // Assert
                assertNotNull(result);
                assertEquals(userResponseDto.getId(), result.getId());
                assertEquals(userResponseDto.getUsername(), result.getUsername());
                verify(userRepository).findById(1L);
                verify(userMapper).toUsuarioResponseDto(user);
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el usuario no existe")
            void findById_ConIdNoExistente_LanzaExcepcion() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

                // Act & Assert
                assertThrows(NoSuchElementException.class, () -> userService.findById(999L));
                verify(userRepository).findById(999L);
                verify(userMapper, never()).toUsuarioResponseDto(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe guardar un nuevo usuario correctamente")
            void save_ConDatosValidos_GuardaUsuario() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
                when(userMapper.toUsuario(any(UserCreateRequestDto.class))).thenReturn(user);
                when(userRepository.save(any(User.class))).thenReturn(user);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.save(userCreateRequestDto);

                // Assert
                assertNotNull(result);
                assertEquals(userResponseDto.getUsername(), result.getUsername());
                verify(userRepository).findByUsernameIgnoreCase(userCreateRequestDto.getUsername());
                verify(userMapper).toUsuario(userCreateRequestDto);
                verify(userRepository).save(any(User.class));
                verify(userMapper).toUsuarioResponseDto(user);
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el username ya existe")
            void save_ConUsernameExistente_LanzaExcepcion() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));

                // Act & Assert
                assertThrows(UserBadRequest.class, () -> userService.save(userCreateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userCreateRequestDto.getUsername());
                verify(userRepository, never()).save(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe actualizar un usuario correctamente")
            void update_ConDatosValidos_ActualizaUsuario() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
                when(userMapper.toUsuario(any(UserUpdateRequestDto.class), any(User.class))).thenReturn(user);
                when(userRepository.save(any(User.class))).thenReturn(user);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.update(1L, userUpdateRequestDto);

                // Assert
                assertNotNull(result);
                verify(userRepository).findById(1L);
                verify(userMapper).toUsuario(userUpdateRequestDto, user);
                verify(userRepository).save(user);
                verify(userMapper).toUsuarioResponseDto(user);
            }

            @Test
            @DisplayName("Debe actualizar usuario cuando el username es el mismo")
            void update_ConMismoUsername_ActualizaUsuario() {
                // Arrange
                userUpdateRequestDto.setUsername("testuser"); // Mismo username
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));
                when(userMapper.toUsuario(any(UserUpdateRequestDto.class), any(User.class))).thenReturn(user);
                when(userRepository.save(any(User.class))).thenReturn(user);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.update(1L, userUpdateRequestDto);

                // Assert
                assertNotNull(result);
                verify(userRepository).findById(1L);
                verify(userRepository).save(user);
            }

            @Test
            @DisplayName("Debe actualizar usuario cuando el username es null")
            void update_ConUsernameNull_ActualizaUsuario() {
                // Arrange
                userUpdateRequestDto.setUsername(null);
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userMapper.toUsuario(any(UserUpdateRequestDto.class), any(User.class))).thenReturn(user);
                when(userRepository.save(any(User.class))).thenReturn(user);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.update(1L, userUpdateRequestDto);

                // Assert
                assertNotNull(result);
                verify(userRepository).findById(1L);
                verify(userRepository, never()).findByUsernameIgnoreCase(anyString());
                verify(userRepository).save(user);
            }

            @Test
            @DisplayName("Debe actualizar usuario cuando el username está vacío")
            void update_ConUsernameVacio_ActualizaUsuario() {
                // Arrange
                userUpdateRequestDto.setUsername("");
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userMapper.toUsuario(any(UserUpdateRequestDto.class), any(User.class))).thenReturn(user);
                when(userRepository.save(any(User.class))).thenReturn(user);
                when(userMapper.toUsuarioResponseDto(any(User.class))).thenReturn(userResponseDto);

                // Act
                UserResponseDto result = userService.update(1L, userUpdateRequestDto);

                // Assert
                assertNotNull(result);
                verify(userRepository).findById(1L);
                verify(userRepository, never()).findByUsernameIgnoreCase(anyString());
                verify(userRepository).save(user);
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el usuario no existe")
            void update_ConIdNoExistente_LanzaExcepcion() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

                // Act & Assert
                assertThrows(NoSuchElementException.class, () -> userService.update(999L, userUpdateRequestDto));
                verify(userRepository).findById(999L);
                verify(userRepository, never()).save(any(User.class));
            }

            @Test
            @DisplayName("Debe lanzar excepción cuando el username ya existe en otro usuario")
            void update_ConUsernameExistenteEnOtroUsuario_LanzaExcepcion() {
                // Arrange
                User otroUsuario = User.builder()
                        .id(2L)
                        .username("updateduser")
                        .build();

                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(otroUsuario));

                // Act & Assert
                assertThrows(UserBadRequest.class, () -> userService.update(1L, userUpdateRequestDto));
                verify(userRepository).findById(1L);
                verify(userRepository).findByUsernameIgnoreCase(userUpdateRequestDto.getUsername());
                verify(userRepository, never()).save(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("DeleteById Tests")
    class DeleteByIdTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe borrar un usuario correctamente")
            void deleteById_ConIdExistente_BorraUsuario() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
                doNothing().when(userRepository).updateIsDeletedById(anyLong(), anyBoolean());

                // Act
                userService.deleteById(1L);

                // Assert
                verify(userRepository).findById(1L);
                verify(userRepository).updateIsDeletedById(1L, true);
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el usuario no existe")
            void deleteById_ConIdNoExistente_LanzaExcepcion() {
                // Arrange
                when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

                // Act & Assert
                assertThrows(NoSuchElementException.class, () -> userService.deleteById(999L));
                verify(userRepository).findById(999L);
                verify(userRepository, never()).updateIsDeletedById(anyLong(), anyBoolean());
            }
        }
    }

    @Nested
    @DisplayName("CheckUserCreate Tests")
    class CheckUserCreateTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe validar correctamente cuando el username no existe")
            void checkUserCreate_ConUsernameNoExistente_NoLanzaExcepcion() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

                // Act & Assert
                assertDoesNotThrow(() -> userService.checkUserCreate(userCreateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userCreateRequestDto.getUsername());
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el username ya existe")
            void checkUserCreate_ConUsernameExistente_LanzaExcepcion() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));

                // Act & Assert
                assertThrows(UserBadRequest.class, () -> userService.checkUserCreate(userCreateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userCreateRequestDto.getUsername());
            }
        }
    }

    @Nested
    @DisplayName("CheckUserUpdate Tests")
    class CheckUserUpdateTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {

            @Test
            @DisplayName("Debe validar correctamente cuando el username no existe")
            void checkUserUpdate_ConUsernameNoExistente_NoLanzaExcepcion() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

                // Act & Assert
                assertDoesNotThrow(() -> userService.checkUserUpdate(1L, userUpdateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userUpdateRequestDto.getUsername());
            }

            @Test
            @DisplayName("Debe validar correctamente cuando el username es del mismo usuario")
            void checkUserUpdate_ConMismoUsername_NoLanzaExcepcion() {
                // Arrange
                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));

                // Act & Assert
                assertDoesNotThrow(() -> userService.checkUserUpdate(1L, userUpdateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userUpdateRequestDto.getUsername());
            }

            @Test
            @DisplayName("Debe validar correctamente cuando el username es null")
            void checkUserUpdate_ConUsernameNull_NoLanzaExcepcion() {
                // Arrange
                userUpdateRequestDto.setUsername(null);

                // Act & Assert
                assertDoesNotThrow(() -> userService.checkUserUpdate(1L, userUpdateRequestDto));
                verify(userRepository, never()).findByUsernameIgnoreCase(anyString());
            }

            @Test
            @DisplayName("Debe validar correctamente cuando el username está vacío")
            void checkUserUpdate_ConUsernameVacio_NoLanzaExcepcion() {
                // Arrange
                userUpdateRequestDto.setUsername("");

                // Act & Assert
                assertDoesNotThrow(() -> userService.checkUserUpdate(1L, userUpdateRequestDto));
                verify(userRepository, never()).findByUsernameIgnoreCase(anyString());
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {

            @Test
            @DisplayName("Debe lanzar excepción cuando el username ya existe en otro usuario")
            void checkUserUpdate_ConUsernameExistenteEnOtroUsuario_LanzaExcepcion() {
                // Arrange
                User otroUsuario = User.builder()
                        .id(2L)
                        .username("updateduser")
                        .build();

                when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(otroUsuario));

                // Act & Assert
                assertThrows(UserBadRequest.class, () -> userService.checkUserUpdate(1L, userUpdateRequestDto));
                verify(userRepository).findByUsernameIgnoreCase(userUpdateRequestDto.getUsername());
            }
        }
    }
}