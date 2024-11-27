package com.mildo.user.Vo;

import lombok.Data;

@Data
public class UserVO {

    private String userId;              // 회원 아이디 ex) #G090
    private String studyId;             // 스터디 아이디
    private int userNumber;             // 사용자 번호
    private String userName;            // 사용자 이름
    private String userGoogleId;        // 구글 아이디
    private String userEmail;           // 이메일
    private String userParticipant;     // 스터디 참가여부
    private String userLeader;          // 리더 여부
    private int userSolvedProblem;      // 해결 문제 수
    private String userTheme;           // 유저 테마

    public UserVO(){}


}
