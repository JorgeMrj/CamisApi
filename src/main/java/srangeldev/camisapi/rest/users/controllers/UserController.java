package srangeldev.camisapi.rest.users.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted
    ) {
        log.info("Buscando todos los usuarios con los siguientes filtros: username={}, email={}, isDeleted={}",
                username.orElse("N/A"), email.orElse("N/A"), isDeleted.map(String::valueOf).orElse("N/A"));
        List<UserResponseDto> users = userService.findAll(username, email, isDeleted);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Buscando usuario por id: {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        log.info("Creando nuevo usuario: {}", userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreateRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id: {} y datos: {}", id, userUpdateRequestDto);
        return ResponseEntity.ok(userService.update(id, userUpdateRequestDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Eliminando usuario con id: {}", id);
        userService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
