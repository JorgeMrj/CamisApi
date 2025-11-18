package srangeldev.camisapi.rest.pedidos.exceptions;

/**
 * Exception para tipo de errores HTTP 404 Not Found => El pedido que se ha solicitado No EXiste
 */
public class PedidoNotFoundException extends PedidoException {
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
