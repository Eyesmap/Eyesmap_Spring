package com.spring.eyesmap.domain.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "report_deletion")
@RequiredArgsConstructor
@Getter
public class ReportDeletion {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "report_deletion_id", columnDefinition = "VARCHAR(255)")
    private String reportDeletionId;
    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;
    private Long userId;

    public ReportDeletion(Report report, Long userId) {
        this.report = report;
        this.userId = userId;
    }
}
