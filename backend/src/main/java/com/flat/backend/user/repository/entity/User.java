package com.flat.backend.user.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flat.backend.property.repository.entity.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flat.backend.friends.repository.entity.Friends;
import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.token.entity.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;
    private String email;
    private String name;
    private String profile;
    private String address;
    @Column(name = "address_lat")
    private String addressLat;
    @Column(name = "address_lng")
    private String addressLng;
    private String password;
    private String phoneNumber;

    // TODO: 탈퇴한 회원을 위한 active 처리 구현이 필요하다.
    // private String active;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "token_id")
    private Token token;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Property> properties = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Friends> friends = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL)
    private List<ReqFriendDto> sentFriendRequests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiverId", cascade = CascadeType.ALL)
    private List<ReqFriendDto> receivedFriendRequests = new ArrayList<>();

}
