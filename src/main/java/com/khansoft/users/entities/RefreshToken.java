package com.khansoft.users.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Rayhan
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken extends Base implements Serializable {
    @Serial
    private static final long serialVersionUID = 6929482536229723029L;
    @Column(name = "token")
    private String token;
    @Column(name = "expiry_date")
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

}
