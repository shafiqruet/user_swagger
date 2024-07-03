package com.khansoft.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "password_change_history")

public class PasswordChangeHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 6929482536229723019L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String password;

    @Column(name = "created_at")
    private Date createdAt;
}
