package com.spring.eyesmap.global.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public class ReportEnum {
    /**
     * DAMAGE: 파손 신고
     * RESTORE: 복구 신고
     * COMPLETE: 신고 처리 완료
     */
    public enum ReportedStatus {
        DAMAGE, RESTORE, COMPLETE
    }
    public enum Sort{
        DOTTED_BLOCK, LINEAR_BLOCK, ACOUSTIC_SIGNAL, VOICE_GUIDE, BRAILLE_SIGN, TACTILE_MAP
    }
    public enum DamagedStatus{
        NORMAL, BAD, SEVERE
    }
    public enum DeleteReason{
        FALSE_REPORT, DUPLICATE
    }

    @JsonCreator
    public static ReportEnum.Sort parsing(String inputValue) {
        return Stream.of(ReportEnum.Sort.values())
                .filter(sort -> sort.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
