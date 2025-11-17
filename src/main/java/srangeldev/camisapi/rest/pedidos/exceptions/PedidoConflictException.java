package srangeldev.camisapi.rest.pedidos.exceptions;

/**
 * Exception para tipo de errores HTTP 409 Conflict ==> Conflicto con los estados del Pedido
 */
public class PedidoConflictException extends PedidoException {
    public PedidoConflictException(String message) {super(message);}

}
