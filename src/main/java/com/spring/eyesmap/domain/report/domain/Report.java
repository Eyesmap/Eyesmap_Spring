package com.spring.eyesmap.domain.report.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity(name = "report")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "report_id", columnDefinition = "VARCHAR(255)")
    private String reportId;

    @Column(name = "dangerous_count")
    @ColumnDefault("0")
    private Integer dangerousCnt;

    @Enumerated(EnumType.STRING)
    private ReportEnum.Sort sort;
    @Enumerated(EnumType.STRING)
    private ReportEnum.DamagedStatus damagedStatus;

    @Enumerated(EnumType.STRING)
    private ReportEnum.ReportedStatus reportedStatus;

    private String contents;
    private String title;

//    private Integer gu;//구 번호

    @CreatedDate
    @Column(updatable = false, name = "report_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "id")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Builder
    public Report(ReportEnum.Sort sort, ReportEnum.DamagedStatus damagedStatus, ReportEnum.ReportedStatus reportedStatus, String contents, String title, Account account, Location location){
        this.sort = sort;
        this.damagedStatus = damagedStatus;
        this.reportedStatus = reportedStatus;
        this.contents = contents;
        this.title = title;
        this.account = account;
        this.location = location;

    }
}
