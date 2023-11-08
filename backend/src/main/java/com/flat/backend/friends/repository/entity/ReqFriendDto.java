package com.flat.backend.friends.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "req_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverId;
}
