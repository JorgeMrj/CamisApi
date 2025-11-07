package srangeldev.camisapi.rest.productos.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Camisetas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El equipo no puede estar vacío")
    @Size(max = 50, message = "El equipo no puede tener más de 50 caracteres")
    private String equipo;

    @NotBlank(message = "La liga no puede estar vacía")
    @Size(max = 50, message = "La liga no puede tener más de 50 caracteres")
    private String liga;

    @NotBlank(message = "La temporada no puede estar vacía")
    @Pattern(
            regexp = "^[0-9]{4}/[0-9]{4}$",
            message = "Formato de temporada inválido (Ejemplo: 2024/2025)"
    )
    private String temporada;

    @NotNull(message = "El tipo de producto es obligatorio")
    private Tipo tipo;

    @Size(max = 255, message = "La descripción es demasiado larga (máximo 255 caracteres)")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @NotNull(message = "La imagen es obligatoria")
    private String imagen;
}


