package com.flat.backend.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flat.backend.property.repository.entity.Property;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity(name = "chatRoom")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id")
    private Long roomId;

    private String sender;
    private String receiver;

    @OneToOne
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;


    @JsonIgnore
    @OneToMany(mappedBy = "")
    private List<ChatMessage> messages = new ArrayList<>();
}
