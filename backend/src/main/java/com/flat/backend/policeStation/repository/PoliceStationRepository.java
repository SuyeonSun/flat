package com.flat.backend.policeStation.repository;

import com.flat.backend.policeStation.repository.entity.PoliceStationLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStationLocation, Long> {
}
