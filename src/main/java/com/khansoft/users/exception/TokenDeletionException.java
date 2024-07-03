package com.khansoft.users.exception;

public class TokenDeletionException extends RuntimeException {
    public TokenDeletionException(String message) {
        super(message);
    }

    public TokenDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
