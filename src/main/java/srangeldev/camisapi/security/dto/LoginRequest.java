package srangeldev.camisapi.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO simple para peticiones de login
 */
@Data
public class LoginRequest {
    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    @NotBlank(message = "La password no puede estar vacía")
    private String password;
}
