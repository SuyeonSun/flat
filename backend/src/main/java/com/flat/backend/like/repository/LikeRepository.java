package com.flat.backend.like.repository;

import com.flat.backend.like.repository.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPropertyId(UUID userId, Long propertyId);
    List<Like> findByPropertyId(Long propertyId);
}
