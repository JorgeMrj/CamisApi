package srangeldev.camisapi.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import srangeldev.camisapi.security.service.AuthService;

/**
 * Carga datos iniciales simples para testing
 * Implementación básica para estudiantes de DAW
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityDataLoader implements CommandLineRunner {

    private final AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Intentando cargar usuarios por defecto...");

            authService.createDefaultUser();
            authService.createTestUser();

            log.info("✓ Usuarios creados exitosamente:");
            log.info("  - Admin: username=admin, password=admin123");
            log.info("  - User:  username=user,  password=user123");

        } catch (Exception e) {
            log.warn("⚠️ No se pudieron crear usuarios por defecto (probablemente MongoDB no disponible)");
            log.warn("   Para testing JWT, asegúrate de tener MongoDB ejecutándose en localhost:27017");
            log.warn("   Error: {}", e.getMessage());

            // No lanzamos la excepción para que la aplicación siga funcionando
            // Solo los endpoints que requieren autenticación fallarán
        }
    }
}
