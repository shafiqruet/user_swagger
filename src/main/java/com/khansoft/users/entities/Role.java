package com.khansoft.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 6929482536229723029L;
    @Column(length = 20, name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)  // Can keep lazy fetching here
    @JsonIgnore
    private Collection<Users> users;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)  // Can keep lazy fetching here
    @JoinTable(name = "roles_authorities", joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id"))
    private Collection<Authority> authorities;

    public Role(String name, Collection<Authority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    public Role(String name) {
    }

}
