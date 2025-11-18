package srangeldev.camisapi.rest.carrito.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo Carrito - PostgreSQL (JPA)
 * 
 * Guarda los productos que el usuario tiene temporalmente en su carrito.
 * Es una entidad transaccional que garantiza la integridad de las reservas.
 * 
 * Comportamiento:
 * - Cuando se añade un producto, se almacena su ID (referencia a MongoDB)
 * - Mantiene la lista de IDs de productos del carrito
 * - Los productos se consultan desde MongoDB cuando sea necesario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {
    
    /**
     * Primary Key autogenerada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Referencia al ID del Usuario en MongoDB
     */
    @NotNull(message = "El usuario no puede ser nulo")
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * Lista de IDs de productos en el carrito
     * Los productos están en MongoDB, aquí solo se almacenan sus IDs
     */
    @ElementCollection
    @CollectionTable(name = "carrito_productos", joinColumns = @JoinColumn(name = "carrito_id"))
    @Column(name = "producto_id")
    @Builder.Default
    private List<String> productosIds = new ArrayList<>();

    /**
     * Fecha de última modificación del carrito
     * Se actualiza automáticamente con cada cambio
     */
    @UpdateTimestamp
    @Column(name = "modificado_en", nullable = false)
    @Builder.Default
    private LocalDateTime modificadoEn = LocalDateTime.now();
    
    /**
     * Fecha de creación del carrito
     */
    @Column(name = "creado_en", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();
}
