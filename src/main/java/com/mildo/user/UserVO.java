package com.mildo.user;

import lombok.Data;

@Data
public class UserVO {

    private String userId;              // 회원 아이디 ex) #G090
    private int userNumber;             // 사용자 번호
    private String userName;            // 사용자 이름
    private String userGoogleId;        // 구글 아이디
    private String userEmail;           // 이메일
    private String userParticipant;     // 스터디 참가여부
    private String userLeader;          // 리더 여부
    private int userSolvedProblem;      // 해결 문제 수

    public UserVO(){}

    public UserVO(String userId, int userNumber, String userName, String userGoogleId, String userEmail, String userParticipant, String userLeader, int userSolvedProblem) {
        this.userId = userId;
        this.userNumber = userNumber;
        this.userName = userName;
        this.userGoogleId = userGoogleId;
        this.userEmail = userEmail;
        this.userParticipant = userParticipant;
        this.userLeader = userLeader;
        this.userSolvedProblem = userSolvedProblem;
    }
}
