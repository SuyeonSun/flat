package com.flat.backend.data;

import com.flat.backend.property.repository.entity.Property_temp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTempRepository extends JpaRepository<Property_temp, Long> {
}
