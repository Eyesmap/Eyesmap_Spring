package com.spring.eyesmap.global.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DistrictNum {
    DOBONG_GU("도봉", 0),  NOWON_GU("노원", 1), GANGBUK_GU("강북", 2),
    EUNPYEONG_GU("은평", 3),SEONGBUK_GU("성북", 4), JUNGNANG_GU("중랑", 5),
    JONGNO_GU("종로", 6),DONGDAEMUN_GU("동대문", 7), SEODAEMUN_GU("서대문", 8),
    GANGDONG_GU("강동", 9),JUNG_GU("중", 10),GANGSEO_GU("강서", 11),
    MAPO_GU("마포", 12), SEONGDONG_GU("성동", 13), YONGSAN_GU("용산", 14),
    GWANGJIN_GU("광진", 15), YEONGDEUNGPO_GU("영등포", 16), YANGCHEON_GU("양천", 17),
    DONGJAK_GU("동작", 18), SONGPA_GU("송파", 19), GANGNAM_GU("강남", 20),
    SEOCHO_GU("서초", 21), GWANAK_GU("관악", 22), GEUMCHEON_GU("금천", 23),
    GURO_GU("구로", 24);

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
