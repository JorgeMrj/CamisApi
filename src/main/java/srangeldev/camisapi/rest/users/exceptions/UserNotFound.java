package srangeldev.camisapi.rest.users.exceptions;

import srangeldev.camisapi.rest.usuarios.exceptions.UserException;

public class UserNotFound extends UserException {
    public UserNotFound(String username) {
        super("Usuario con username " +  username + " no encontrado");
    }
}
