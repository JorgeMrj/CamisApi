package srangeldev.camisapi.rest.pedidos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 📦 Modelo Pedido - PostgreSQL (JPA)
 * 
 * Representa una compra confirmada y registrada en el sistema.
 * Incluye un snapshot completo de los productos en el momento de la venta.
 * 
 * Características importantes:
 * - Es INMUTABLE una vez creado (no se modifican los productos)
 * - Contiene una copia de los datos del producto (DetallePedido)
 * - Garantiza coherencia histórica aunque cambien los productos en el catálogo
 * - Es la fuente de verdad para facturación e historial de compras
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    /**
     * Primary Key autogenerada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Referencia al ID del Usuario en PostgreSQL
     * Se almacena el ID del User (Long)
     */
    @NotNull(message = "El usuario no puede ser nulo")
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * Estado actual del pedido
     * Valores: PENDIENTE_PAGO, PAGADO, ENVIADO, ENTREGADO, CANCELADO
     */
    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.PENDIENTE_PAGO;
    
    /**
     * Fecha de creación del pedido
     * Se establece automáticamente al crear el pedido
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    /**
     * Total del pedido en euros
     * Suma de todos los precios pagados por los productos
     */
    @NotNull(message = "El total no puede ser nulo")
    @Positive(message = "El total debe ser positivo")
    @Column(nullable = false)
    private Double total;
    
    /**
     * Lista de detalles del pedido (productos vendidos)
     * Cada DetallePedido es un snapshot inmutable del producto vendido
     * 
     * IMPORTANTE: Son copias de los datos, no referencias a MongoDB
     */
    @ElementCollection
    @CollectionTable(
        name = "pedido_detalles",
        joinColumns = @JoinColumn(name = "pedido_id")
    )
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();
    
    /**
     * Dirección de envío (opcional)
     */
    @Column(name = "direccion_envio", length = 500)
    private String direccionEnvio;
    
    /**
     * Fecha de pago (se establece cuando el estado cambia a PAGADO)
     */
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    /**
     * Fecha de envío (se establece cuando el estado cambia a ENVIADO)
     */
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    /**
     * Número de seguimiento del envío (opcional)
     */
    @Column(name = "numero_seguimiento", length = 100)
    private String numeroSeguimiento;
}
