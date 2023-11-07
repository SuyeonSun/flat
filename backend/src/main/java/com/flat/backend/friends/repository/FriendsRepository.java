package com.flat.backend.friends.repository;

import com.flat.backend.friends.repository.entity.Friends;
import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    Optional<List<Friends>> findByFriendId_Id(UUID userId); // User 테이블의 ID와 Friends 테이블의 friendId 조인
}
