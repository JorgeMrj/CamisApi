package srangeldev.camisapi.rest.users.exceptions;

public class UserNotFound extends UserException {
    public UserNotFound(String username) {
        super("Usuario con username " +  username + " no encontrado");
    }
}
