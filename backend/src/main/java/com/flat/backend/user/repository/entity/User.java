package com.flat.backend.user.repository.entity;

import com.flat.backend.token.entity.Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    private String email;

    private String password;

    // TODO: 탈퇴한 회원을 위한 active 처리 구현이 필요하다.
    // private String active;

    @OneToOne
    @JoinColumn(name = "token_id")
    private Token token;
}
