package srangeldev.camisapi.rest.users.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.users.models.Rol;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @Size(min = 4, max = 30, message = "El nombre de usuario debe tener entre 4 y 30 caracteres")
    private String username;

    private Set<Rol> roles;
}