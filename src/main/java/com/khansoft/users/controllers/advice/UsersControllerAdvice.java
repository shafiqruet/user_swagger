/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.controllers.advice;

import com.khansoft.users.exception.UserNotFoundException;
import com.khansoft.users.exception.DataSaveException;
import com.khansoft.users.exception.DuplicateEntryException;
import com.khansoft.users.exception.EmailAndPhoneAlreadyExistsException;
import com.khansoft.users.exception.InvalidDataTypeException;
import com.khansoft.users.exception.MethodArgumentException;
import com.khansoft.users.exception.TokenCreationException;
import com.khansoft.users.exception.UsernameAndOrPasswordInvalidException;
import com.khansoft.users.response.RestApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hafiz.khan
 */
@RestControllerAdvice
@Slf4j
public class UsersControllerAdvice {

    private static final Logger LOGGER = LogManager.getLogger(UsersControllerAdvice.class);
    private static final String MESSAGE_SOURCE = "khansoft";
    private static final String GENERIC_FAILURE_MSG = "Something went wrong";

    //@Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new RestApiError(new Date(), status.value(), status, ex.getMessage()), status);
    }

    //@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new RestApiError(new Date(), status.value(), status, ex.getMessage()), status);
    }

    //@Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new RestApiError(new Date(), status.value(), status, ex.getMessage()), status);
    }

    //@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new RestApiError(new Date(), status.value(), status, ex.getMessage()), status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return new ResponseEntity<>(new RestApiError(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    ResponseEntity<Object> handleUserNotFoundException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new RestApiError(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAndOrPasswordInvalidException.class)
    @ResponseBody
    ResponseEntity<Object> handleUsernameAndOrPasswordInvalidException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new RestApiError(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenCreationException.class)
    @ResponseBody
    ResponseEntity<Object> handleTokenCreationException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new RestApiError(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /* @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    } */

    @ExceptionHandler(DuplicateEntryException.class)
    public final ResponseEntity<Object> handleDuplicateEntryException(DuplicateEntryException ex, HttpServletRequest request) {
        log.error("Duplicate entry exception handled. Message : {}", ex.getMessage());

        ExceptionDataReturnDetail returnDetails = ExceptionDataReturnDetail.builder()
                .code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).errorDetails(ex.getMessage())
                .messageSource(MESSAGE_SOURCE).build();
        return new ResponseEntity<>(returnDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAndPhoneAlreadyExistsException.class)
    public final ResponseEntity<Object> handleDuplicateEmailAndPhoneException(EmailAndPhoneAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Duplicate entry exception handled. Message : {}", ex.getMessage());

        ExceptionDataReturnDetail returnDetails = ExceptionDataReturnDetail.builder()
                .code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).errorDetails(ex.getMessage())
                .messageSource(MESSAGE_SOURCE).build();
        return new ResponseEntity<>(returnDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataSaveException.class)
    public final ResponseEntity<Object> handleDataSaveException(DataSaveException ex, HttpServletRequest request) {
        log.error("Data save exception handled. Message : {}", ex.getMessage());

        ExceptionDataReturnDetail returnDetails = ExceptionDataReturnDetail.builder()
                .code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).errorDetails(ex.getMessage())
                .messageSource(MESSAGE_SOURCE).build();
        return new ResponseEntity<>(returnDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentException ex, HttpServletRequest request) {
        log.error("Request argeument exception handled. Message : {}", ex.getMessage());

        ExceptionDataReturnDetail returnDetails = ExceptionDataReturnDetail.builder()
                .code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).errorDetails(ex.getMessage())
                .messageSource(MESSAGE_SOURCE).build();
        return new ResponseEntity<>(returnDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidDataTypeException.class)
    public final ResponseEntity<Object> handleInvalidDataType(InvalidDataTypeException ex, HttpServletRequest request) {
        log.error("Request argeument exception handled. Message : {}", ex.getMessage());

        ExceptionDataReturnDetail returnDetails = ExceptionDataReturnDetail.builder()
                .code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).errorDetails(ex.getMessage())
                .messageSource(MESSAGE_SOURCE).build();
        return new ResponseEntity<>(returnDetails, HttpStatus.CONFLICT);
    }


     @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, List<String>> errorMap = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            errorMap = fieldErrors.stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errorMap);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
