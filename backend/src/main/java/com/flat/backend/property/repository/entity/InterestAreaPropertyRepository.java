package com.flat.backend.property.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestAreaPropertyRepository extends JpaRepository<InterestAreaProperty, Long> {

    List<InterestAreaProperty> findByEmail(String email);

    void deleteByPropertyId(Long id);

    void deleteByEmail(String email);
}
