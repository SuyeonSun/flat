package com.flat.backend.property.repository;

import com.flat.backend.property.repository.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAll(Pageable pageable);
}
