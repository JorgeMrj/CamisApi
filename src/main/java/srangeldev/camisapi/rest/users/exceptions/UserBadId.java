package srangeldev.camisapi.rest.users.exceptions;

import srangeldev.camisapi.rest.users.exceptions.UserException;

public class UserBadId extends UserException {
    public UserBadId(String message) {
        super(message);
    }

}
