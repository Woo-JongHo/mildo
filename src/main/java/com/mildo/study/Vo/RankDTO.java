package com.mildo.study.Vo;

import lombok.Data;

@Data
public class RankDTO {

    private int rank;
    private String userName;
    private int solvedProblem;
    private String userId;
    private String studyId;

    public RankDTO(){}

    public RankDTO(int rank, String userName, int solvedProblem, String userId, String studyId) {
        this.rank = rank;
        this.userName = userName;
        this.solvedProblem = solvedProblem;
        this.userId = userId;
        this.studyId = studyId;
    }
}
