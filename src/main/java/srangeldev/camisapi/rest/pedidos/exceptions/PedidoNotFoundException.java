package srangeldev.camisapi.rest.pedidos.exceptions;

/**
 * Exception para tipo de errores HTTP 404 Not Found => El pedido que se ha solicitado No EXiste
 */
public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
