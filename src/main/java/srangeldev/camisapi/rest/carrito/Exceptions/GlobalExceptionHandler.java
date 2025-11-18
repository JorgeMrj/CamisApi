package srangeldev.camisapi.rest.carrito.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejo global de excepciones para el módulo de carrito
 * Permite que los tests funcionen correctamente
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarritoNotFound.class)
    public ResponseEntity<Map<String, Object>> handleCarritoNotFound(CarritoNotFound e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Carrito no encontrado");
        error.put("message", e.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CarritoBadId.class)
    public ResponseEntity<Map<String, Object>> handleCarritoBadId(CarritoBadId e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "ID de carrito inválido");
        error.put("message", e.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CarritoException.class)
    public ResponseEntity<Map<String, Object>> handleCarritoException(CarritoException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error en carrito");
        error.put("message", e.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> error = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();

        error.put("error", "Datos de entrada inválidos");
        error.put("message", "Error de validación");
        error.put("timestamp", LocalDateTime.now());

        Map<String, String> fieldErrors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError ->
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        error.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("message", e.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
