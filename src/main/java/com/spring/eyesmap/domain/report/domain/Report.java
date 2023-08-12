package com.spring.eyesmap.domain.report.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.global.enumeration.Sort;
import com.spring.eyesmap.global.enumeration.Status;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

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

    private String contents;

    private Status status;

    private String title;
    private Integer gu;//구 번호

    @CreatedDate
    @Column(updatable = false, name = "report_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location locationId;

    @ManyToOne
    @JoinColumn(name = "iamge_id")
    private Image image;
}
