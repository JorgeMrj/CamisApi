package srangeldev.camisapi.rest.pedidos.exceptions;

/**
 * Exception para tipo de errores HTTP 400 Bad Request => datos enviados invalidos
 */
public class PedidoBadRequestException extends PedidoException {
    public PedidoBadRequestException(String message) {super(message);}
}
