/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *
 * @author hafiz.khan
 */
@Data
@AllArgsConstructor
public class RestApiError {
    Date timestamp;
    Integer status;
    HttpStatus error;
    String message;
}
