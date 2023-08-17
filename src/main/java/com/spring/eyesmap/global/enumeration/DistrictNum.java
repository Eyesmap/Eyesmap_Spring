package com.spring.eyesmap.global.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DistrictNum {
    DOBONG_GU("도봉", 1), GANGBUK_GU("강북", 2), NOWON_GU("노원", 3),
    SEONGBUK_GU("성북", 4), JUNGNANG_GU("중랑", 5), DONGDAEMUN_GU("동대문", 6),
    JONGNO_GU("종로", 7), SEONGDONG_GU("성동", 8), GWANGJIN_GU("광진", 9),
    JUNG_GU("중", 10), EUNPYEONG_GU("은평", 11), SEODAEMUN_GU("서대문", 12),
    MAPO_GU("마포", 13), YONGSAN_GU("용산", 14), GANGDONG_GU("강동", 15),
    SONGPA_GU("송파", 16), GANGNAM_GU("강난", 17), SEOCHO_GU("서초", 18),
    DONGJAK_GU("동작", 19), GWANAK_GU("관악", 20), GEUMCHEON_GU("금천", 21),
    YEONGDEUNGPO_GU("영등포", 22), YANGCHEON_GU("양천", 23), GANGSEO_GU("강서", 24),
    GURO_GU("구로", 25);

    final private String guName;
    final private Integer guNum;

    private DistrictNum(String guName, Integer guNum) {
        this.guName = guName;
        this.guNum = guNum;
    }
    public String getName() {
        return this.guName;
    }
    public Integer getNum() {
        return this.guNum;
    }

    public static DistrictNum nameOf(String guName) {
        for (DistrictNum enumGu : DistrictNum.values()) {
            if (enumGu.getName().equals(guName)) {
                return enumGu;
            }
        }
        return null;
    }
}
