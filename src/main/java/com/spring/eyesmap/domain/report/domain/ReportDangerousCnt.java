package com.spring.eyesmap.domain.report.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "report_dangerous_cnt")
@RequiredArgsConstructor
@Getter
public class ReportDangerousCnt {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "report_dangerous_cnt_id", columnDefinition = "VARCHAR(255)")
    private String reportDangerousCntId;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    private Long userId;

    public ReportDangerousCnt(Report report, Long userId) {
        this.report = report;
        this.userId = userId;
    }
}
