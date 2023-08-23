package com.spring.eyesmap.domain.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "location")
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "gpsX", "gpsY" }) })
public class Location {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "location_id", columnDefinition = "VARCHAR(255)")
    private String id;
    private String address;
    private String gpsX;
    private String gpsY;

    @Builder
    public Location(String address, String gpsX, String gpsY){
        this.address = address;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }
}
