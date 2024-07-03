package com.khansoft.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DataSaveException extends RuntimeException {

    public DataSaveException() {
    }

    public DataSaveException(String message) {
        super(message);
    }
}