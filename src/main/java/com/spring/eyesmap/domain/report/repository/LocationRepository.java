package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {
    Location findByAddress(String address);

}
