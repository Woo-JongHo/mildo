package com.mildo.study.Vo;

import lombok.Data;

import java.sql.Date;

@Data
public class RemainingDaysDTO {

//    private String studyId;
    private String remainingDays;
    private String elapsedDays;

    public RemainingDaysDTO(){}

    public RemainingDaysDTO(String remainingDays, String elapsedDays) {
        this.remainingDays = remainingDays;
        this.elapsedDays = elapsedDays;
    }

}
