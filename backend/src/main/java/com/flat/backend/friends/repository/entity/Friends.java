package com.flat.backend.friends.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friendId;

    @Builder.Default
    private int grade = FriendGrade.BEST.getValue();
}
