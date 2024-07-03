package com.khansoft.users.entities.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.UsersPreferences;
@Getter
@Setter
public class UserRegister implements Serializable {

    private Long id;
    @NotBlank
    @Size(min = 2, max = 30, message = "First name is required")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30, message = "Last name is required ")
    private String lastName;
    @NotNull
    @Past
    private Date birthDate;
    private String company;
    private String companyId;
    @NotBlank
    private String countryCode;
    private String password;
    private String phone;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotNull
    private String zip;

    @Email(message = "Email should be valid")
    @NotBlank
    private String email;
    private String userId;
    private String referenceNo;
    @NotNull
    private Integer userType;
    @NotNull
    private Integer usersTermsAndConditions;
    private List<Users> users;
    private UsersPreferences usersPreferences;

    @NotNull(message = "Roles cannot be null")
    @Size(min = 1, message = "At least one role is required")
    private List<@Valid RoleRequest> roles;
}
