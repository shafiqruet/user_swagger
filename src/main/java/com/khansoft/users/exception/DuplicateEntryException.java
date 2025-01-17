package com.khansoft.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException() {
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}