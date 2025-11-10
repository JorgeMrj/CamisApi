package srangeldev.camisapi.rest.usuarios.exceptions;

public class UsuarioNotFound extends UsuarioException {
    public UsuarioNotFound(String username) {
        super("Usuario con username " +  username + " no encontrado");
    }
}
