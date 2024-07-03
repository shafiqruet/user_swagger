package com.khansoft.users.exception;

public class ClientIdNotFoundException extends RuntimeException {
    public ClientIdNotFoundException(String message) {
        super(message);
    }
}
