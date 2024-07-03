package com.khansoft.users.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.request.UserRegister;
import com.khansoft.users.entities.response.UserResponse;

/**
 * @author Rayhan
 */

public interface UsersService extends UserDetailsService {

    UserResponse createUser(UserRegister userDetails);
    //UserResponse createUser(AdminUserRequest userDetails);

    UserDetails loadUserByUsername(String username);

    UserResponse getUserDetailsByEmail(String email);

    UserResponse getUserById(Long id);

    UserResponse getUserDetailsByUserName(String username);

    Users findByUsername(String username);

    Users findByUsernameOrEmailOrPhone(String identifier);

    public void increaseFailedAttempts(Users user);

    public void resetFailedAttempts(Users user);

    public void lockAccount(Users user);
}
