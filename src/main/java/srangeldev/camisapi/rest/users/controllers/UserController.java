package srangeldev.camisapi.rest.users.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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

/**
 * Controlador REST para gestión de usuarios con MongoDB
 * Siguiendo el patrón del profesor
 */
@RestController
@RequestMapping("${api.version}/users")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Obtiene un usuario por su ID (ObjectId)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        log.info("Buscando usuario por id: {}", id);
        
        // Validar que el ID es un ObjectId válido
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new UserBadId("El ID proporcionado no es válido: " + id);
        }
        
        return ResponseEntity.ok(userService.findById(objectId));
    }

    /**
     * Busca usuarios por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<UserResponseDto>> getUsersByNombre(@PathVariable String nombre) {
        log.info("Buscando usuarios por nombre: {}", nombre);
        return ResponseEntity.ok(userService.findByNombre(nombre));
    }

    /**
     * Crea un nuevo usuario
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        log.info("Creando nuevo usuario: {}", userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreateRequestDto));
    }

    /**
     * Actualiza un usuario existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable String id, 
            @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id: {} y datos: {}", id, userUpdateRequestDto);
        
        // Validar que el ID es un ObjectId válido
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new UserBadId("El ID proporcionado no es válido: " + id);
        }
        
        return ResponseEntity.ok(userService.update(objectId, userUpdateRequestDto));
    }

    /**
     * Elimina un usuario (borrado físico)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        log.info("Eliminando usuario con id: {}", id);
        
        // Validar que el ID es un ObjectId válido
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new UserBadId("El ID proporcionado no es válido: " + id);
        }
        
        userService.deleteById(objectId);
    }
}
