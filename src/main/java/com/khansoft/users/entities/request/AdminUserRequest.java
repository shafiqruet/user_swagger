package com.khansoft.users.entities.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rayhan
 */

@Getter
@Setter
public class AdminUserRequest implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String password;
    private String username;
    private List<RoleRequest> roles;
    private boolean mfaActivated;
}
