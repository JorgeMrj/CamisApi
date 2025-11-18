package srangeldev.camisapi.rest.users.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserBadId;
import srangeldev.camisapi.rest.users.services.UserService;

import java.util.List;

@RestController
@RequestMapping("${api.version}/users")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuarios", description = "API para gestión de usuarios (Requiere rol ADMIN)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Recupera una lista completa de todos los usuarios registrados (Solo ADMIN)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Recupera un usuario específico mediante su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
        @Parameter(description = "ID único del usuario", required = true) @PathVariable Long id) {
        log.info("Buscando usuario por id: {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Busca usuarios que contengan el nombre especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<UserResponseDto>> getUsersByNombre(
        @Parameter(description = "Nombre del usuario a buscar", required = true) @PathVariable String nombre) {
        log.info("Buscando usuarios por nombre: {}", nombre);
        return ResponseEntity.ok(userService.findByNombre(nombre));
    }

    @Operation(
        summary = "Crear nuevo usuario",
        description = "Crea un nuevo usuario con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
        @Parameter(description = "Datos del usuario a crear", required = true)
        @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        log.info("Creando nuevo usuario: {}", userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreateRequestDto));
    }

    @Operation(
        summary = "Actualizar usuario existente",
        description = "Actualiza un usuario existente con los nuevos datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
        @Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Long id,
        @Parameter(description = "Nuevos datos del usuario", required = true)
        @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id: {} y datos: {}", id, userUpdateRequestDto);
        return ResponseEntity.ok(userService.update(id, userUpdateRequestDto));
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario específico del sistema (borrado físico)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Requiere rol ADMIN"
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
        @Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable Long id) {
        log.info("Eliminando usuario con id: {}", id);
        userService.deleteById(id);
    }
}
