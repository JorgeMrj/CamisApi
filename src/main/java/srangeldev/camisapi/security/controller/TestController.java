package srangeldev.camisapi.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de prueba para verificar autenticación
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Este endpoint es público - no requiere autenticación");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hola " + auth.getName() + "! Este endpoint requiere autenticación JWT");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hola " + auth.getName() + "! Tienes permisos de administrador");
    }
}
