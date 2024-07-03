package com.khansoft.users.entities;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.khansoft.users.enumeration.BooleanStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_type")
public class UsersType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id", updatable = false)
    private Integer clientId;

    @Column(unique = true, nullable = false)
    private String code;

    @NotBlank
    @Column(length = 20) 
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL) 
    private BooleanStatus status = BooleanStatus.NO;


    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
