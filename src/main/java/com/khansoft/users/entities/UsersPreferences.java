package com.khansoft.users.entities;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khansoft.users.enumeration.BooleanStatus;
import com.khansoft.users.enumeration.DoubleIncomeOption;
import com.khansoft.users.enumeration.Status;
import com.khansoft.users.enumeration.Visibility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Table(name = "users_preferences")
public class UsersPreferences implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @Column(name = "client_id", updatable = false)
    private Integer clientId;

    @Column(name = "language_id")
    private Integer langaugeId;

    @Column(name = "mail_template_id")
    private Integer mailTemplateId;

    @Column(name = "is_mfa_activated")
    private boolean isMfaActivated;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "mfa_updated_at")
    private Date mfaUpdatedAt;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public UsersPreferences(Long id) {
        this.id = id;
    }

}
