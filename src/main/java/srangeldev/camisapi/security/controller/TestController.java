package srangeldev.camisapi.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Endpoints de Prueba", description = "Endpoints para probar diferentes niveles de acceso")
public class TestController {

    @Operation(
        summary = "Endpoint público",
        description = "Endpoint de acceso público que no requiere autenticación"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Acceso exitoso sin autenticación requerida"
        )
    })
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Este endpoint es público - no requiere autenticación");
    }

    @Operation(
        summary = "Endpoint privado",
        description = "Endpoint que requiere autenticación JWT válida"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Acceso exitoso con autenticación válida"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o faltante"
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/private")
    public ResponseEntity<String> privateEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hola " + auth.getName() + "! Este endpoint requiere autenticación JWT");
    }

    @Operation(
        summary = "Endpoint de administrador",
        description = "Endpoint que requiere permisos de administrador"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Acceso exitoso con permisos de administrador"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o faltante"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Sin permisos de administrador"
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hola " + auth.getName() + "! Tienes permisos de administrador");
    }
}
