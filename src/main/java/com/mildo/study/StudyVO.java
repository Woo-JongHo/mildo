package com.mildo.study;

import lombok.Data;

import java.sql.Date;

@Data
public class StudyVO {

    private int studyId;            // 스터디 아이디
    private String userId;          // 회원 아이디 ex) #G090
    private String studyCode;       // 스터디 코드
    private String studyName;       // 스터디 이름
    private String studyPassword;   // 스터디 비밀번호
    private Date studyStart;        // 스터디 시작일
    private Date studyEnd;          // 스터디 끝 나는 일


    public StudyVO(String userId, String studyCode, String studyName, String studyPassword, Date studyStart, Date studyEnd) {
        this.studyId = 0;
        this.userId = userId;
        this.studyCode = studyCode;
        this.studyName = studyName;
        this.studyPassword = studyPassword;
        this.studyStart = studyStart;
        this.studyEnd = studyEnd;
    }
}
