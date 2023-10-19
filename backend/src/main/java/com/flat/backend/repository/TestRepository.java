package com.flat.backend.repository;

import com.flat.backend.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TestRepository extends JpaRepository<Test, Long> {

}
