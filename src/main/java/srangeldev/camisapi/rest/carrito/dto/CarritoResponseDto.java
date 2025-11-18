package srangeldev.camisapi.rest.carrito.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.productos.models.Producto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para el carrito
 * Contiene toda la información del carrito para enviar al cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDto {

    /**
     * ID único del carrito
     */
    private Long id;

    /**
     * ID del usuario propietario del carrito
     */
    private Long userId;

    /**
     * Lista de IDs de productos en el carrito
     * Referencias a productos en MongoDB
     */
    private List<Producto> productos;

    /**
     * Número total de items en el carrito
     */
    private Integer totalProductos;

    /**
     * Fecha de última modificación
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modificadoEn;

    /**
     * Fecha de creación del carrito
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creadoEn;
}
