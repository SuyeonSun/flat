package com.flat.backend.token.entity;

import com.flat.backend.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "token")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id")
    private UUID id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne(mappedBy = "token")
    private User user;
}
