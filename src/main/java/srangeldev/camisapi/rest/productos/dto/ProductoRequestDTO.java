package srangeldev.camisapi.rest.productos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede tener más de 200 caracteres")
    private String nombre;

    @NotBlank(message = "El equipo no puede estar vacío")
    @Size(max = 100, message = "El equipo no puede tener más de 100 caracteres")
    private String equipo;

    @NotBlank(message = "La talla no puede estar vacía")
    @Size(max = 10, message = "La talla no puede tener más de 10 caracteres")
    private String talla;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser positivo")
    private Double precio;

    @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
    @Pattern(regexp = "^(http|https)://.*$", message = "La URL de la imagen debe ser válida y comenzar con http o https")
    private String imageUrl;

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoProducto estado;
}
