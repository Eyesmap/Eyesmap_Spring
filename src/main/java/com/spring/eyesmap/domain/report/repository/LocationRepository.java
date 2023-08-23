package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {
    Optional<Location> findByGpsXAndGpsY(String gpsX, String gpsY);
    boolean existsByGpsXAndGpsY(String gpsX, String gpsY);

}
