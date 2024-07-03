package com.khansoft.users.services;

import org.springframework.http.ResponseEntity;

import com.khansoft.users.entities.request.LoginRequest;

import java.util.Map;

public interface LoginService {
    ResponseEntity<Map<String, String>> login(LoginRequest loginRequest);
}
