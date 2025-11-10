package srangeldev.camisapi.rest.productos.models;

/**
 * 🔑 Enum EstadoProducto
 * 
 * Define los tres estados posibles de un producto (camiseta única).
 * Este enum sustituye completamente el concepto de "stock".
 * 
 * Ciclo de vida del producto:
 * 1. DISPONIBLE → El producto está listo para ser comprado
 * 2. RESERVADO → El producto está en el carrito de un usuario (reserva temporal)
 * 3. VENDIDO → El producto ha sido vendido y ya no está disponible
 */
public enum EstadoProducto {
    /**
     * Producto disponible para la venta
     */
    DISPONIBLE,
    
    /**
     * Producto reservado temporalmente en un carrito
     */
    RESERVADO,
    
    /**
     * Producto vendido, ya no disponible
     */
    VENDIDO
}
