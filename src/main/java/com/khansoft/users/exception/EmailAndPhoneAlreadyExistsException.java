package com.khansoft.users.exception;

public class EmailAndPhoneAlreadyExistsException extends RuntimeException {

    public EmailAndPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
