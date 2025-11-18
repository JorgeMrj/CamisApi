package srangeldev.camisapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 * Implementación básica para estudiantes de DAW
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "CamisAPI - API de Camisetas de Fútbol",
        description = """
            API REST para gestión de camisetas de fútbol, usuarios, carritos y pedidos.
            
            ## Funcionalidades principales:
            - 🛍️ **Productos**: Gestión completa de camisetas de fútbol
            - 👥 **Usuarios**: Registro y administración de usuarios
            - 🛒 **Carritos**: Manejo de carritos de compra
            - 📦 **Pedidos**: Gestión de pedidos y estados
            - 🔐 **Autenticación**: Sistema JWT para seguridad
            - 📊 **GraphQL**: Consultas avanzadas disponibles
            
            ## Tecnologías utilizadas:
            - Spring Boot 3.5.7
            - Spring Security con JWT
            - MongoDB (Usuarios y Productos)
            - PostgreSQL (Carritos y Pedidos)
            - Redis (Cache)
            
            ## Autenticación:
            Para endpoints protegidos, incluir el token JWT en el header:
            ```
            Authorization: Bearer <tu-token-jwt>
            ```
            """,
        version = "1.0.0",
        contact = @Contact(
            name = "Estudiante DAW",
            email = "estudiante@daw.com",
            url = "https://github.com/estudiante/camisapi"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Servidor de Desarrollo",
            url = "http://localhost:8081"
        ),
        @Server(
            description = "Servidor Docker",
            url = "http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Introduce el token JWT obtenido del endpoint /auth/login"
)
public class OpenApiConfig {
}
