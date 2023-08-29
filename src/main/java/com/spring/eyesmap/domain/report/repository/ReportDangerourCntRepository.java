package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDangerourCntRepository extends JpaRepository<ReportDangerousCnt, String>{
    Optional<ReportDangerousCnt> findByReportReportIdAndUserId(String reportId, Long userId);
    boolean existsByReportReportIdAndUserId(String reportId, Long userId);
    List<ReportDangerousCnt> findByUserId(Long userId);
}
