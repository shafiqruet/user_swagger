package com.khansoft.users.entities.request;


import com.khansoft.users.entities.Authority;

import lombok.Getter;
import lombok.Setter;


import java.util.Optional;

@Setter
@Getter

public class RoleRequest {
    private String name;
    private Optional<Authority> authorities;
}
