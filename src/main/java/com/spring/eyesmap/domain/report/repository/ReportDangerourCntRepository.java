package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDangerourCntRepository extends JpaRepository<ReportDangerousCnt, String> {
    List<ReportDangerousCnt> findByUserId(Long userId);
}
