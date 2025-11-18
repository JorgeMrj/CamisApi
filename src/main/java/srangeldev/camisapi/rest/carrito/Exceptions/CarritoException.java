package srangeldev.camisapi.rest.carrito.Exceptions;

public abstract class CarritoException extends RuntimeException {
    public CarritoException(String message) {
        super(message);
    }
}
