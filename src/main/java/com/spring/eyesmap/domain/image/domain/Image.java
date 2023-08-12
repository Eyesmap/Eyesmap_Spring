package com.spring.eyesmap.domain.image.domain;

import com.spring.eyesmap.global.enumeration.ImageSort;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "image")
public class Image {
    @Id
    private String id;

    private String url;

    @Enumerated(EnumType.STRING)
    private ImageSort imageSort; //damaged, repaird

}
