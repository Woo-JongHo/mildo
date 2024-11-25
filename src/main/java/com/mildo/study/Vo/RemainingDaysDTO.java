package com.mildo.study.Vo;

import lombok.Data;

@Data
public class RemainingDaysDTO {

    private int studyId;
    private int remainingDays;
    private int elapsedDays;

    public RemainingDaysDTO(){}

    public RemainingDaysDTO(int studyId, int remainingDays, int elapsedDays) {
        this.studyId = studyId;
        this.remainingDays = remainingDays;
        this.elapsedDays = elapsedDays;
    }
}
