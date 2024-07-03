package com.khansoft.users.components.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    public ApiResponse(String message) {
        super();
        this.message = message;
    }

    public ApiResponse(Object responseDetails, String message) {
        super();
        this.responseDetails = responseDetails;
        this.message = message;
    }

    public ApiResponse(Object responseDetails, String message, Integer statusCode) {
        super();
        this.responseDetails = responseDetails;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ApiResponse(Object responseDetails, String message, String exceptionDetails) {
        super();
        this.responseDetails = responseDetails;
        this.message = message;
        this.exceptionDetails = exceptionDetails;
    }
    

    private Object responseDetails;
    private String message;
    private Integer statusCode;

    @JsonIgnore
    private String exceptionDetails;
}