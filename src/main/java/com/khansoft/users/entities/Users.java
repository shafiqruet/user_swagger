package com.khansoft.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khansoft.users.enumeration.UsersStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "client_id"}),
        @UniqueConstraint(columnNames = {"phone", "client_id"}),
        @UniqueConstraint(columnNames = {"reference_no", "client_id"})
})
public class Users implements Serializable {

    @Serial
    private static final long serialVersionUID = -1725678149216053L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id", updatable = false)
    private Integer clientId;

    @NotBlank
    @Size(min = 5, message = "Password too short (min 5 characters)")
    @Column(name = "password")
    private String password;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "company")
    private String company;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "vat_id")
    private String vatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_uid", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private Users sponsorId;

    @Column(name = "sponsor")
    @Exclude
    private String sponsorUsername;

    @Exclude
    private String token;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "email_verified_at")
    private Date emailVerifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false)
    private UsersStatus status = UsersStatus.on;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JsonIgnore
    private UsersPreferences usersPreferences;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JoinColumn(name = "password_change_history",referencedColumnName = "user_id")
    private List<PasswordChangeHistory> passwordChangeHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_conditions_id")
    private UsersTermsAndConditions usersTermsAndConditions;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id", referencedColumnName = "code")
    private UsersType userType;

    @Column(name = "locked_at")
    private Date lockedAt;

    @Column(name = "password_updated_at")
    private Date passwordUpdatedAt;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    Collection<Role> roles;

    @Column(name = "mfa_activated")
    private boolean mfaActivated;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "failed_attempts")
    private Integer failedAttempts = 0;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    
    
    /* public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        roles.stream().flatMap(role -> role.getAuthorities().stream()).forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getName())));
        return authorities;
    } */
}