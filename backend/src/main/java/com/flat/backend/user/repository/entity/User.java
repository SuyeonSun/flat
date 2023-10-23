package com.flat.backend.user.repository.entity;

import com.flat.backend.token.entity.Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "User")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String password;

    // TODO: do it later.
    // private String active;

    @OneToOne
    @JoinColumn(name = "token_id")
    private Token token;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token=" + token +
                '}';
    }
}
