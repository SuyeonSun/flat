package com.flat.backend.friends.repository;

import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReqFriendRepository extends JpaRepository<ReqFriendDto, Long> {

    Optional<ReqFriendDto> findBySenderId_IdAndReceiverId_Id(UUID senderId, UUID receiverId);
    Optional<List<ReqFriendDto>> findBySenderId_Id(UUID userId); // User 테이블의 ID와 ReqFriendDto 테이블의 senderId를 조인
    Optional<List<ReqFriendDto>> findByReceiverId_Id(UUID userId); // User 테이블의 ID와 ReqFriendDto 테이블의 receiverId를 조인
}
