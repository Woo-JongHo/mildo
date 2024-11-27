package com.mildo.study.Vo;

import lombok.Data;

@Data
public class RankDTO {

    private int rank;
    private String userName;
    private int solvedProblem;

    public RankDTO(){}

    public RankDTO(int rank, String userName, int solvedProblem) {
        this.rank = rank;
        this.userName = userName;
        this.solvedProblem = solvedProblem;
    }
}
