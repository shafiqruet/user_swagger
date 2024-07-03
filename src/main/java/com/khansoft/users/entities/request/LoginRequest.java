/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * @author hafiz.khan
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @Size(min = 5, max = 50)
    private String identifier;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;
    private int twoFactorCode;
}
