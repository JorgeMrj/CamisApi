package srangeldev.camisapi.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.security.JwtUtil;
import srangeldev.camisapi.security.dto.AuthResponse;
import srangeldev.camisapi.security.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para autenticación y manejo de JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica a un usuario y retorna un token JWT para acceder a endpoints protegidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso - Token JWT generado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @Parameter(description = "Credenciales de acceso", required = true)
        @Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar las credenciales
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Cargar el usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // Generar token
            String token = jwtUtil.generateToken(userDetails);

            // Respuesta exitosa
            AuthResponse response = AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .message("Login exitoso")
                .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            AuthResponse response = AuthResponse.builder()
                .message("Credenciales incorrectas")
                .build();
            return ResponseEntity.status(401).body(response);
        }
    }

    @Operation(
        summary = "Verificar autenticación",
        description = "Endpoint de prueba para verificar que el token JWT es válido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token válido - Acceso autorizado"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido o expirado"
        )
    })
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Acceso autorizado - JWT funciona correctamente");
    }
}
