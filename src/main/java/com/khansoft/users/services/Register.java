package com.khansoft.users.services;
import org.springframework.http.HttpHeaders;

import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.request.UserRegister;

public interface  Register {
    Users createUser(UserRegister request, HttpHeaders headers);
}
