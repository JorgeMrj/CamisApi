package srangeldev.camisapi.rest.users.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Modelo User - MongoDB
 * 
 * Almacena la información de usuarios del sistema.
 * Es el único modelo que se guarda en MongoDB según la arquitectura híbrida.
 * 
 * Utiliza @Document para indicar que es un documento de MongoDB
 * y @TypeAlias para indicar con qué clase se van a mapear a la hora de recuperarlos de la base de datos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("users")
@TypeAlias("User")
public class User implements UserDetails {
    /**
     * Identificador único del usuario en MongoDB usando ObjectId
     */
    @Id
    private ObjectId id;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long idUsuario;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 30, message = "El nombre de usuario debe tener entre 4 y 30 caracteres")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    private Set<Rol> roles;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Builder.Default
    private Boolean isDeleted = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
