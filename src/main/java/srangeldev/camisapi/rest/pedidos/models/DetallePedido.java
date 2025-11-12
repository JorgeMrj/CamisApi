package srangeldev.camisapi.rest.pedidos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo DetallePedido - PostgreSQL (JPA)
 * 
 * Subentidad que guarda los datos INMUTABLES del producto al momento de la venta.
 * Actúa como una "foto" o snapshot del producto en el momento exacto de la compra.
 * 
 * ¿Por qué es necesario?
 * Si el producto se modifica o elimina en PostgreSQL, el pedido seguirá siendo coherente
 * - Permite mantener un histórico exacto de lo que se vendió y a qué precio
 * - Garantiza que los datos del pedido no cambien aunque cambie el catálogo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DetallePedido {
    
    /**
     * Referencia al ID del producto en PostgreSQL
     * Se mantiene para trazabilidad, aunque el producto puede ya no existir
     */
    @NotNull(message = "El ID del producto no puede ser nulo")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    /**
     * Nombre del producto en el momento de la venta
     * Snapshot inmutable
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    /**
     * Talla del producto vendido
     */
    @NotBlank(message = "La talla no puede estar vacía")
    @Column(name = "talla", nullable = false, length = 10)
    private String talla;
    
    /**
     * Equipo del producto vendido
     */
    @NotBlank(message = "El equipo no puede estar vacío")
    @Column(name = "equipo", nullable = false, length = 100)
    private String equipo;
    
    /**
     * Precio pagado por el producto en el momento de la venta
     * Este es el precio final, puede incluir descuentos
     */
    @NotNull(message = "El precio pagado no puede ser nulo")
    @Positive(message = "El precio pagado debe ser positivo")
    @Column(name = "precio_pagado", nullable = false)
    private Double precioPagado;
    
    /**
     * URL de la imagen del producto (opcional)
     * Snapshot de la imagen en el momento de la venta
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
