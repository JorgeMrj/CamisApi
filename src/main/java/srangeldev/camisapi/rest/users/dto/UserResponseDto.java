package srangeldev.camisapi.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.users.models.Rol;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String nombre;
    private String username;
    private Set<Rol> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}