package srangeldev.camisapi.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO simple para respuestas de autenticación
 */
@Data
@Builder
public class AuthResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String username;
    private String message;
}
