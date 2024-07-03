/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author hafiz.khan
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found for id: " + id);
    }

    public UserNotFoundException(String username) {
        //throw new UnsupportedOperationException("Not supported yet.");
        super("User not found for username: " + username);
    }
}
