package com.spring.eyesmap.domain.report.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.eyesmap.domain.user.domain.User;
import com.spring.eyesmap.global.enumeration.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;


@Entity(name = "report")
@Getter

public class Report {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "report_id", columnDefinition = "BINARY(16)")
    private String reportId;

    @Column(name = "dangerous_count")
    private Integer dangerousCnt;

    @Enumerated(EnumType.STRING)
    private Sort sort;

    @Column(name = "api_report_id")
    private String apiReportId; //서울시 api 고유 번호

    private String contents;

    private Status status;

    private String title;

    @CreatedDate
    @Column(updatable = false, name = "report_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id")
//    private Location locationId;
}
