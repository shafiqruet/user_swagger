package com.khansoft.users.entities.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.khansoft.users.entities.Role;

/**
 * @author Rayhan
 */

@Getter
@Setter
public class UserResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String userName;
    private String password;
    private List<Role> roles;
}
