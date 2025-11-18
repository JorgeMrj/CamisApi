package srangeldev.camisapi.security.controller;

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

/**
 * Controlador simple para autenticación
 * Implementación básica para estudiantes de DAW
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Endpoint para login - genera JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
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

    /**
     * Endpoint de prueba para verificar autenticación
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Acceso autorizado - JWT funciona correctamente");
    }
}
