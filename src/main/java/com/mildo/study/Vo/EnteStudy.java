package com.mildo.study.Vo;

import lombok.Data;

@Data
public class EnteStudy {

    private String studyId;
    private String studyPassword;
    private String userId;

    public EnteStudy(){}

    public EnteStudy(String studyId, String studyPassword, String userId) {
        this.studyId = studyId;
        this.studyPassword = studyPassword;
        this.userId = userId;
    }
}
