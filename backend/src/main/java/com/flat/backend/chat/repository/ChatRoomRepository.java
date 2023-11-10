package com.flat.backend.chat.repository;

import com.flat.backend.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "select * from chat_room c where c.sender = :user or c.receiver = :user", nativeQuery = true)
    Optional<List<ChatRoom>> findByUser(@Param("user") String user);
    Optional<List<ChatRoom>> findByReceiver(String receiver);

    Optional<ChatRoom> findBySenderAndReceiver(String sender, String receiver);
}
