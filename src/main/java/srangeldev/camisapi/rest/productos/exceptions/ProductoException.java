package srangeldev.camisapi.rest.productos.exceptions;

public abstract class ProductoException extends RuntimeException {
    public ProductoException(String message) {
        super(message);
    }
}
