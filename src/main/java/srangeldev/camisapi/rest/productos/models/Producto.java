package srangeldev.camisapi.rest.productos.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

/**
 * Modelo Producto - MongoDB
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("productos")
@TypeAlias("Producto")
public class Producto {

    /**
     * Identificador único del producto
     */
    @Id
    private String id;

    /**
     * Nombre del producto
     * Ejemplo: "Camiseta Local Real Madrid 2024/25"
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede tener más de 200 caracteres")
    @Indexed
    private String nombre;

    /**
     * Nombre del club o selección
     * Ejemplo: "Real Madrid", "FC Barcelona", "Selección España"
     */
    @NotBlank(message = "El equipo no puede estar vacío")
    @Size(max = 100, message = "El equipo no puede tener más de 100 caracteres")
    @Indexed
    private String equipo;

    /**
     * Talla del producto
     * Ejemplo: "S", "M", "L", "XL", "XXL"
     */
    @NotBlank(message = "La talla no puede estar vacía")
    @Size(max = 10, message = "La talla no puede tener más de 10 caracteres")
    private String talla;

    /**
     * Descripción detallada del producto
     */
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    /**
     * Precio del producto
     */
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser positivo")
    private Double precio;

    /**
     * URL de la imagen del producto
     */
    @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "La URL de la imagen debe ser válida y comenzar con http o https"
    )
    private String imageUrl;

    /**
     * Estado del producto
     * Valores posibles: DISPONIBLE, RESERVADO, VENDIDO
     */
    @NotNull(message = "El estado no puede ser nulo")
    private EstadoProducto estado;

    /**
     * Fecha en la que se añadió el producto
     */
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    @Builder.Default
    private LocalDate fechaCreacion = LocalDate.now();
}
