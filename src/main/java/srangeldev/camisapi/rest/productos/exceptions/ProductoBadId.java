package srangeldev.camisapi.rest.productos.exceptions;

public class ProductoBadId  extends ProductoException {
        public ProductoBadId(String uuid) {
            super("UUID: " + uuid + " no válido o de formato incorrecto");
        }
    }
