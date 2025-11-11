package srangeldev.camisapi.rest.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String equipo;
    private String talla;
    private String descripcion;
    private Double precio;
    private String imageUrl;
    private EstadoProducto estado;
    private LocalDateTime fechaCreacion;
}
