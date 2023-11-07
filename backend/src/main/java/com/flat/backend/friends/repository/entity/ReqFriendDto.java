package com.flat.backend.friends.repository.entity;

import com.flat.backend.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "reqFriend")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReqFriendDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column("req_id")
    private Long id

    @OneToOne
    @JoinColumn(name = "friend_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "friend_id")
    private User user;
}
