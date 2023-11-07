package com.flat.backend.friends.repository.entity;

import com.flat.backend.friends.FriendGrade;
import com.flat.backend.user.repository.entity.User;
import lombok.*;
import jakarta.persistence.*;

@Entity(name = "friends")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friends_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "friend_id")
    private User user;

    @Builder.Default
    private int grade = FriendGrade.BEST.getValue();
}
