package com.flat.backend.websocket.repository;

import com.flat.backend.websocket.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findById(String id);

    void deleteById(String id);
}
