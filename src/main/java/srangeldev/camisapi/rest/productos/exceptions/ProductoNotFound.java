package srangeldev.camisapi.rest.productos.exceptions;

public class ProductoNotFound extends ProductoException {
    public ProductoNotFound(Long id) {
        super("Producto con id " + id + " no encontrado");
    }
}
