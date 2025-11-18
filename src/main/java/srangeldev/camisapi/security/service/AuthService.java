package srangeldev.camisapi.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import srangeldev.camisapi.rest.users.models.Rol;
import srangeldev.camisapi.security.entity.UserEntity;
import srangeldev.camisapi.security.repository.UserEntityRepository;

import java.util.Set;

/**
 * Servicio simple para gestión de usuarios usando H2
 * Implementación básica para estudiantes de DAW
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crear usuario por defecto para pruebas
     */
    public UserEntity createDefaultUser() {
        if (userRepository.existsByUsername("admin")) {
            return userRepository.findByUsername("admin").get();
        }

        UserEntity user = UserEntity.builder()
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
    public UserEntity createTestUser() {
        if (userRepository.existsByUsername("user")) {
            return userRepository.findByUsername("user").get();
        }

        UserEntity user = UserEntity.builder()
                .nombre("Usuario Test")
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles(Set.of(Rol.USER))
                .build();

        return userRepository.save(user);
    }
}
