/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.khansoft.users.entities.request.LoginRequest;

/*
 *
 * @author hafiz.khan
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameAndOrPasswordInvalidException extends RuntimeException{
    
    public UsernameAndOrPasswordInvalidException(LoginRequest loginRequest) {
        super("Email and/or Password invalid! " + "Email: " + loginRequest.getIdentifier());
    }        
    
}

