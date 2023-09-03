package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {
    Optional<Location> findByGpsXAndGpsY(Double gpsX, Double gpsY);
    boolean existsByGpsXAndGpsY(Double gpsX, Double gpsY);
}
