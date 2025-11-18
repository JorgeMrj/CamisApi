package srangeldev.camisapi.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;

import java.util.Set;

/**
 * Servicio simple para gestión de usuarios usando MongoDB
 * Implementación básica para estudiantes de DAW
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crear usuario por defecto para pruebas
     */
    public User createDefaultUser() {
        if (userRepository.findByUsername("admin").isPresent()) {
            return userRepository.findByUsername("admin").get();
        }

        User user = User.builder()
                .idUsuario(1L)
                .nombre("Administrador")
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles(Set.of(Rol.ADMIN))
                .build();

        return userRepository.save(user);
    }

    /**
     * Crear usuario normal para pruebas
     */
    public User createTestUser() {
        if (userRepository.findByUsername("user").isPresent()) {
            return userRepository.findByUsername("user").get();
        }

        User user = User.builder()
                .idUsuario(2L)
                .nombre("Usuario Test")
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles(Set.of(Rol.USER))
                .build();

        return userRepository.save(user);
    }
}
