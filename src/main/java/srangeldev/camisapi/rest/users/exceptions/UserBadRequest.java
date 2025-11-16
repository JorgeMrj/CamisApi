package srangeldev.camisapi.rest.users.exceptions;

import srangeldev.camisapi.rest.users.exceptions.UserException;

public class UserBadRequest extends UserException {
    public UserBadRequest(String message) {
        super(message);
    }
}