package com.spring.eyesmap.domain.image.repository;

import com.spring.eyesmap.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findAllByReportReportId(String reportId);
}
