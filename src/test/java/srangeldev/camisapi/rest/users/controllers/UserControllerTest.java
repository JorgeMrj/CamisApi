package srangeldev.camisapi.rest.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import srangeldev.camisapi.rest.handler.GlobalExceptionHandler;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserNotFound;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.rest.users.services.UserService;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    // Define la ruta base con el valor real del property
    private final String API_BASE_URL = "/api/v1/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserResponseDto userResponseDto;
    private UserCreateRequestDto userCreateRequestDto;
    private UserUpdateRequestDto userUpdateRequestDto;

    private final String validId = "507f1f77bcf86cd799439011";
    private final String invalidId = "1234";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar message converter con ObjectMapper
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);

        // Configurar MockMvc con placeholders, GlobalExceptionHandler y message converters
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(messageConverter)
                .addPlaceholderValue("api.version", "/api/v1")
                .build();

        userResponseDto = UserResponseDto.builder()
                .id(new ObjectId(validId).toHexString())
                .nombre("Test User")
                .username("testuser")
                .roles(Set.of(Rol.USER)) // CORREGIDO: Usar String, no Enum
                .build();

        userCreateRequestDto = UserCreateRequestDto.builder()
                .nombre("New User")
                .username("newuser")
                .password("password123")
                .roles(Set.of(Rol.USER)) // CORREGIDO: Usar String, no Enum
                .build();

        userUpdateRequestDto = UserUpdateRequestDto.builder()
                .nombre("Updated User")
                .username("updateduser")
                .roles(Set.of(Rol.ADMIN)) // CORREGIDO: Usar String, no Enum
                .build();
    }

    @Nested
    @DisplayName("GetAllUsers Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Debe devolver lista de usuarios y status 200")
        void getAllUsers_Success() throws Exception {
            when(userService.findAll()).thenReturn(List.of(userResponseDto));

            // CORREGIDO: Usar la URL base completa
            mockMvc.perform(get(API_BASE_URL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].username", is("testuser")));

            verify(userService).findAll();
        }
    }

    @Nested
    @DisplayName("GetUserById Tests")
    class GetUserByIdTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {
            @Test
            @DisplayName("Debe devolver usuario cuando ID es válido y existe")
            void getUserById_Success() throws Exception {
                when(userService.findById(any(ObjectId.class))).thenReturn(userResponseDto);

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(get(API_BASE_URL + "/{id}", validId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.username", is("testuser")));

                verify(userService).findById(any(ObjectId.class));
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {
            @Test
            @DisplayName("Debe lanzar UserBadId cuando el ID no es un ObjectId válido")
            void getUserById_InvalidId_ThrowsException() throws Exception {
                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(get(API_BASE_URL + "/{id}", invalidId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").exists());

                verify(userService, never()).findById(any());
            }

            @Test
            @DisplayName("Debe devolver 404 si el servicio lanza UserNotFound")
            void getUserById_NotFound() throws Exception {
                doThrow(new UserNotFound("No encontrado")).when(userService).findById(any(ObjectId.class));

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(get(API_BASE_URL + "/{id}", validId))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").exists());
            }
        }
    }

    @Nested
    @DisplayName("GetUsersByNombre Tests")
    class GetUsersByNombreTests {
        @Test
        @DisplayName("Debe devolver lista filtrada por nombre")
        void getUsersByNombre_Success() throws Exception {
            when(userService.findByNombre(anyString())).thenReturn(List.of(userResponseDto));

            // CORREGIDO: Usar la URL base completa
            mockMvc.perform(get(API_BASE_URL + "/nombre/{nombre}", "Test")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(userService).findByNombre("Test");
        }
    }

    @Nested
    @DisplayName("CreateUser Tests")
    class CreateUserTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {
            @Test
            @DisplayName("Debe crear usuario y devolver 201 Created")
            void createUser_Success() throws Exception {
                when(userService.save(any(UserCreateRequestDto.class))).thenReturn(userResponseDto);

                String jsonBody = objectMapper.writeValueAsString(userCreateRequestDto);

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(post(API_BASE_URL)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.username", is("testuser")));

                verify(userService).save(any(UserCreateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {
            @Test
            @DisplayName("Debe devolver 400 Bad Request si falla validación (@Valid)")
            void createUser_ValidationFail() throws Exception {
                // Username vacío para forzar error de validación
                userCreateRequestDto.setUsername("");
                String jsonBody = objectMapper.writeValueAsString(userCreateRequestDto);

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(post(API_BASE_URL)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.username").exists());

                verify(userService, never()).save(any());
            }
        }
    }

    @Nested
    @DisplayName("UpdateUser Tests")
    class UpdateUserTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {
            @Test
            @DisplayName("Debe actualizar usuario correctamente")
            void updateUser_Success() throws Exception {
                when(userService.update(any(ObjectId.class), any(UserUpdateRequestDto.class)))
                        .thenReturn(userResponseDto);

                String jsonBody = objectMapper.writeValueAsString(userUpdateRequestDto);

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(put(API_BASE_URL + "/{id}", validId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.username", is("testuser")));

                verify(userService).update(any(ObjectId.class), any(UserUpdateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {
            @Test
            @DisplayName("Debe lanzar UserBadId con ID inválido")
            void updateUser_InvalidId() throws Exception {
                String jsonBody = objectMapper.writeValueAsString(userUpdateRequestDto);

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(put(API_BASE_URL + "/{id}", invalidId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").exists());

                verify(userService, never()).update(any(), any());
            }
        }
    }

    @Nested
    @DisplayName("DeleteUser Tests")
    class DeleteUserTests {

        @Nested
        @DisplayName("Casos Correctos")
        class CasosCorrectos {
            @Test
            @DisplayName("Debe eliminar usuario y devolver 204 No Content")
            void deleteUser_Success() throws Exception {
                doNothing().when(userService).deleteById(any(ObjectId.class));

                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(delete(API_BASE_URL + "/{id}", validId))
                        .andExpect(status().isNoContent());

                verify(userService).deleteById(any(ObjectId.class));
            }
        }

        @Nested
        @DisplayName("Casos Incorrectos")
        class CasosIncorrectos {
            @Test
            @DisplayName("Debe lanzar UserBadId con ID inválido")
            void deleteUser_InvalidId() throws Exception {
                // CORREGIDO: Usar la URL base completa
                mockMvc.perform(delete(API_BASE_URL + "/{id}", invalidId))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").exists());

                verify(userService, never()).deleteById(any());
            }
        }
    }
}