package com.spring.eyesmap.global.enumeration;

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
        DOTTED_BLOCK, ACOUSTIC_GUIDENCE_SYSTEM, BRAILLE_INFO_BOARD
    }
    public enum DamagedStatus{
        NORMAL, BAD, SEVERE
    }
    public enum DeleteReason{
        FALSE_REPORT, DUPLICATE
    }
}
