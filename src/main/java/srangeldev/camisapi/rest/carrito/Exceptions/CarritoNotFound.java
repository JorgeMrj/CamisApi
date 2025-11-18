package srangeldev.camisapi.rest.carrito.Exceptions;

public class CarritoNotFound extends CarritoException {
    public CarritoNotFound(Long id) {
        super("carrito con id " +  id + " no encontrado");
    }
}
