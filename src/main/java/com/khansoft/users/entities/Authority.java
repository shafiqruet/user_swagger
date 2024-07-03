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

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities")
public class Authority extends Base implements Serializable {
    @Serial
    private static final long serialVersionUID = 6929482536229723029L;
    @Column(nullable = false, length = 200, name = "name")
    private String name;
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)  // Changed to LAZY
    @JsonIgnore
    private Collection<Role> roles;

    public Authority(String name) {
        this.name = name;
    }

}
