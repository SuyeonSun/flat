package com.flat.backend.websocket.entity;

import com.flat.backend.user.repository.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom implements Serializable {

    @Id
    private String id;

    @NotNull
    @Column(length = 23)
    private String name;

    private int count;

    @NotNull
    private LocalDateTime expiryDate;

    public ChatRoom(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.count = 0;
        this.expiryDate = now().plusHours(3L);
    }

    public void enter() {
        this.count++;
    }

    public void exit() {
        this.count--;
        if(count < 0) {
            count = 0;
        }
    }

    public boolean isRemovable() {
        if(this.expiryDate.isAfter(now()) && this.count == 0) {
            return false;
        }
        return true;
    }
}
