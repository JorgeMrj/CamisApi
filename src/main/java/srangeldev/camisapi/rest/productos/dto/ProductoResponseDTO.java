package srangeldev.camisapi.rest.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private String id;
    private String nombre;
    private String equipo;
    private String talla;
    private String descripcion;
    private Double precio;
    private String imageUrl;
    private EstadoProducto estado;
    private LocalDate fechaCreacion;
}
