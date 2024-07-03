package com.khansoft.users.controllers;

import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.request.LoginRequest;
import com.khansoft.users.entities.request.UserRegister;
import com.khansoft.users.services.LoginService;
import com.khansoft.users.services.impl.RegisterImpl;
import com.khansoft.users.components.common.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.khansoft.users.util.UrlConstraint;

import static com.khansoft.users.constants.ResponseConstants.SUCCESS_MSG;

import jakarta.validation.Valid;


import java.util.Map;

@RequestMapping(UrlConstraint.AuthManagement.ROOT)
@RestController
public class AuthController {

    private final RegisterImpl registerImpl;
    private final LoginService loginService;

    public AuthController(RegisterImpl registerImpl, LoginService loginService) {
        this.registerImpl = registerImpl;
        this.loginService = loginService;
    }

    /**
     * Endpoint to create a new user.
     *
     * @param request User registration details
     * @param headers HTTP headers
     * @return ResponseEntity containing created user
     * @throws Exception if there is an issue creating the user
     */
    @PostMapping(UrlConstraint.AuthManagement.REGISTER)
    public ApiResponse createUser(@RequestBody  @Valid UserRegister request, @RequestHeader HttpHeaders headers) throws Exception {
        Users createdUser = registerImpl.createUser(request, headers);
        //return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        return new ApiResponse(createdUser, SUCCESS_MSG);
    }

   
}
