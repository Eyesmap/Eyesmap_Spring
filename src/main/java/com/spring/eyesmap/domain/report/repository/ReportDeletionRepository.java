package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.report.domain.ReportDeletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDeletionRepository extends JpaRepository<ReportDeletion, String> {
    void deleteAllByReportReportId(String reportId);
    boolean existsByReportReportIdAndUserId(String reportId, Long userId);
}
