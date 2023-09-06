package com.spring.eyesmap.global.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DistrictNum {
    DOBONG_GU("도봉", 1),  NOWON_GU("노원", 2), GANGBUK_GU("강북", 3),
    EUNPYEONG_GU("은평", 4),SEONGBUK_GU("성북", 5), JUNGNANG_GU("중랑", 6),
    JONGNO_GU("종로", 7),DONGDAEMUN_GU("동대문", 8), SEODAEMUN_GU("서대문", 9),
    GANGDONG_GU("강동", 10),JUNG_GU("중", 11),GANGSEO_GU("강서", 12),
    MAPO_GU("마포", 13), SEONGDONG_GU("성동", 14), YONGSAN_GU("용산", 15),
    GWANGJIN_GU("광진", 16), YEONGDEUNGPO_GU("영등포", 17), YANGCHEON_GU("양천", 18),
    DONGJAK_GU("동작", 19), SONGPA_GU("송파", 20), GANGNAM_GU("강남", 21),
    SEOCHO_GU("서초", 22), GWANAK_GU("관악", 23), GEUMCHEON_GU("금천", 24),
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
    public static DistrictNum numOf(Integer guNum) {
        for (DistrictNum enumGu : DistrictNum.values()) {
            if (enumGu.getNum().equals(guNum)) {
                return enumGu;
            }
        }
        return null;
    }
}
