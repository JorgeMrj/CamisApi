package srangeldev.camisapi.rest.usuarios.exceptions;

public abstract class UsuarioException extends RuntimeException {
    public UsuarioException(String message) {
        super(message);
    }
}
