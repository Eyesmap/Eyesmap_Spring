package com.spring.eyesmap.domain.report.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity(name = "location")
public class Location {
    @Id
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
