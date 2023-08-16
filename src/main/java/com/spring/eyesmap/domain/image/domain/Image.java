package com.spring.eyesmap.domain.image.domain;

import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ImageSort;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity(name = "image")
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "image_id", columnDefinition = "VARCHAR(255)")
    private String id;

    private String url;
    @Column(name = "image_name")
    private String imgNm;

    @Enumerated(EnumType.STRING)
    private ImageSort imageSort; //damaged, repaird

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Builder
    public Image(String url, String imgNm, ImageSort imageSort, Report report){
        this.url = url;
        this.imgNm = imgNm;
        this.imageSort = imageSort;
        this.report = report;
    }
}
