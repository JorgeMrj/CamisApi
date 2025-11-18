package srangeldev.camisapi.rest.carrito.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import srangeldev.camisapi.rest.productos.models.Producto;

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
 * - Cuando se añade un producto, su estado cambia a RESERVADO en PostgreSQL
 * - Si se elimina del carrito o expira, el producto vuelve a DISPONIBLE
 * - Los carritos pueden tener un tiempo de expiración para liberar productos
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
     * Se almacena el ID del User (String - ObjectId de MongoDB)
     */
    @NotNull(message = "El usuario no puede ser nulo")
    @Column(name = "user_id", nullable = false, length = 24)
    private Long userId;
    
    /**
     * Lista de productos en el carrito (relación @OneToMany)
     * Relación unidireccional desde Carrito hacia Producto
     * 
     * IMPORTANTE: Se crea una tabla intermedia carrito_productos automáticamente
     */
    @OneToMany
    @JoinTable(
        name = "carrito_productos",
        joinColumns = @JoinColumn(name = "carrito_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
    
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
