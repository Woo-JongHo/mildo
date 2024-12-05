package com.mildo.user;


import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    // 구글 로그인 회원 조회
    public UserVO findUser(String number) {return UserDBManger.findUser(number);}

    // 회원 아이디 가져오기
    public String findNullUserId(){
        return UserDBManger.findNullUserId();
    }

    // 구글 회원 가입 업데이트
    public void saveUpdateUser(UserVO users) {
        UserDBManger.saveUpdateUser(users);
    }

    // userId로 회원 조회
    public UserVO finduserId(String userId){
        return UserDBManger.finduserId(userId);
    }

    // userId로 토큰 찾기
    public TokenVO findToken(String userId){
        return UserDBManger.findToken(userId);
    }

    // 토큰 저장
    public void saveToken(TokenVO token){
        UserDBManger.saveToken(token);
    }

    // 토큰 업데이트
    public void saveUpdateToken(TokenVO token){
        UserDBManger.saveUpdateToken(token);
    }

    // 필터에서 토큰 조회
    public String findRefreshTokenByUserId(String userId){
        return UserDBManger.findRefreshTokenByUserId(userId);
    }

    // 코드 레벨별로 갯수 가져오기
    public List<LevelCountDTO> solvedLevelsList(String userId){
         return UserDBManger.solvedLevelsList(userId);
    }

    public void updateStudyId(String userId, String studyId) {
        UserDBManger.updateStudyId(userId, studyId);
    }

    public boolean checkExtensionSync(String userId, String studyId) {
        return UserDBManger.checkExtensionSync(userId,studyId);
    }

    @Transactional
    public int studyGetOut(String userId) {
        // 스터디 참가 여부 null로 바꿈
        int result1 = UserDBManger.userIdChangNull(userId);
        // 댓글 삭제
        int result2 = UserDBManger.userIdDeleteComment(userId);
        // 코드 삭제
        int result3 = UserDBManger.userIdDeleteCode(userId);
        // 스터디 탈되로 업데이트
        int result = UserDBManger.studyGetOut(userId);

        return result;
    }
    public void createStudy(String userId, String studyId, Date date) {
        UserDBManger.createStudy(userId, studyId, date);
    }
}
