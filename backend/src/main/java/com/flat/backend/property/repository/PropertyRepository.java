package com.flat.backend.property.repository;

import com.flat.backend.property.repository.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
