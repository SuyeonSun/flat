package com.flat.backend.property.repository;

import com.flat.backend.property.repository.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query(value = "select * from properties p where p.article_feature_desc like concat('%', :searchKeyword, '%') and p.trade_type_name like concat('%', :tradeTypeName, '%') and p.direction like concat('%', :direction, '%')",
            nativeQuery = true)
    Page<Property> findAll(Pageable pageable, @Param("searchKeyword") String searchKeyword, @Param("tradeTypeName") String tradeTypeName, @Param("direction") String direction);
}
