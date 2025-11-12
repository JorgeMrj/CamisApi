package srangeldev.camisapi.rest.pedidos.models;

/**
 * Enum EstadoPedido
 * 
 * Define los estados posibles de un pedido en el sistema.
 * 
 * Flujo típico:
 * 1. PENDIENTE_PAGO → El pedido se ha creado pero no se ha pagado
 * 2. PAGADO → El pago se ha confirmado correctamente
 * 3. ENVIADO → El pedido ha sido enviado al cliente
 * 4. ENTREGADO → El pedido ha sido entregado (opcional)
 * 5. CANCELADO → El pedido ha sido cancelado (opcional)
 */
public enum EstadoPedido {
    /**
     * Pedido creado, esperando confirmación de pago
     */
    PENDIENTE_PAGO,
    
    /**
     * Pago confirmado, listo para ser procesado
     */
    PAGADO,
    
    /**
     * Pedido enviado al cliente
     */
    ENVIADO,
    
    /**
     * Pedido entregado con éxito
     */
    ENTREGADO,
    
    /**
     * Pedido cancelado
     */
    CANCELADO
}
