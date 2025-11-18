package srangeldev.camisapi.rest.carrito.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.models.Carrito;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Mapper para conversiones entre entidad Carrito y DTOs
 * Maneja las transformaciones de datos para la API REST
 */
@Component
public class CarritoMapper {

    /**
     * Convierte un DTO de creación a entidad Carrito
     *
     * @param createDto DTO con datos de creación
     * @return Nueva entidad Carrito
     */
    public Carrito toEntity(CarritoCreateRequestDto createDto) {
        if (createDto == null) {
            return null;
        }

        return Carrito.builder()
                .userId(createDto.getUserId())
                .productosIds(createDto.getProductosIds() != null ? new ArrayList<>(createDto.getProductosIds()) : new ArrayList<>())
                .creadoEn(LocalDateTime.now())
                .modificadoEn(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte una entidad Carrito a DTO de respuesta
     *
     * @param carrito Entidad Carrito
     * @return DTO de respuesta con todos los datos del carrito
     */
    public CarritoResponseDto toResponseDto(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        return CarritoResponseDto.builder()
                .id(carrito.getId())
                .userId(carrito.getUserId())
                .productosIds(carrito.getProductosIds())
                .totalProductos(carrito.getProductosIds() != null ? carrito.getProductosIds().size() : 0)
                .modificadoEn(carrito.getModificadoEn())
                .creadoEn(carrito.getCreadoEn())
                .build();
    }

    /**
     * Actualiza una entidad Carrito existente con datos del DTO de actualización
     *
     * @param carrito Entidad existente a actualizar
     * @param updateDto DTO con los nuevos datos
     * @return La misma entidad actualizada
     */
    public Carrito updateFromDto(Carrito carrito, CarritoUpdateRequestDto updateDto) {
        if (carrito == null || updateDto == null) {
            return carrito;
        }

        // Procesar según la acción especificada
        String accion = updateDto.getAccion();

        if ("REEMPLAZAR".equals(accion) || accion == null) {
            // Reemplazar toda la lista de productos
            if (updateDto.getProductosIds() != null) {
                carrito.setProductosIds(new ArrayList<>(updateDto.getProductosIds()));
            }
        } else if ("AGREGAR".equals(accion)) {
            // Agregar un producto específico por ID
            if (updateDto.getProductosIds() != null && !updateDto.getProductosIds().isEmpty()) {
                for (String productoId : updateDto.getProductosIds()) {
                    if (!carrito.getProductosIds().contains(productoId)) {
                        carrito.getProductosIds().add(productoId);
                    }
                }
            }
        } else if ("QUITAR".equals(accion)) {
            // Quitar productos específicos por ID
            if (updateDto.getProductosIds() != null) {
                carrito.getProductosIds().removeAll(updateDto.getProductosIds());
            }
        } else if ("LIMPIAR".equals(accion)) {
            // Limpiar todo el carrito
            carrito.getProductosIds().clear();
        }

        // La fecha de modificación se actualiza automáticamente con @UpdateTimestamp
        return carrito;
    }


}
