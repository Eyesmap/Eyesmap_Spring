package com.spring.eyesmap.domain.image.repository;

import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findAllByReportReportId(String reportId);

    List<Image> findByReport(Report report);
}
