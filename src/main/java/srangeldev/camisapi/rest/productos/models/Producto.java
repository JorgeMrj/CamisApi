package srangeldev.camisapi.rest.productos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Modelo Producto - PostgreSQL (JPA)
 * 
 * Cada camiseta es ÚNICA, no hay control de stock ni unidades múltiples.
 * El campo `estado` es el núcleo del flujo de venta.
 * 
 * Características importantes:
 * - Cada talla es un producto distinto
 * - No existe concepto de "cantidad" o "stock"
 * - El estado controla si está disponible, reservado o vendido
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    
    /**
     * Identificador único del producto en PostgreSQL
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nombre del producto
     * Ejemplo: "Camiseta Local Real Madrid 2024/25"
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede tener más de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombre;
    
    /**
     * Nombre del club o selección
     * Ejemplo: "Real Madrid", "FC Barcelona", "Selección España"
     */
    @NotBlank(message = "El equipo no puede estar vacío")
    @Size(max = 100, message = "El equipo no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String equipo;
    
    /**
     * Talla del producto
     * Ejemplo: "S", "M", "L", "XL", "XXL"
     * 
     * IMPORTANTE: Cada talla es un producto distinto con su propio ID
     */
    @NotBlank(message = "La talla no puede estar vacía")
    @Size(max = 10, message = "La talla no puede tener más de 10 caracteres")
    @Column(nullable = false, length = 10)
    private String talla;
    
    /**
     * Descripción detallada del producto
     */
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;
    
    /**
     * Precio del producto en euros
     */
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false)
    private Double precio;
    
    /**
     * URL de la imagen del producto
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    /**
     * 🔑 Estado del producto (núcleo del sistema)
     * Valores posibles: DISPONIBLE, RESERVADO, VENDIDO
     * 
     * Flujo de estados:
     * - DISPONIBLE: Puede ser añadido al carrito
     * - RESERVADO: Está en el carrito de un usuario (temporal)
     * - VENDIDO: Compra finalizada, producto no disponible
     */
    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoProducto estado = EstadoProducto.DISPONIBLE;
    
    /**
     * Fecha en la que se añadió el producto al catálogo
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
