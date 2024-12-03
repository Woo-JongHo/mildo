package com.mildo.user;


import com.mildo.code.CodeVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.study.Vo.StudyVO;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    // 구글 로그인 회원 조회
    public UserVO findUser(String number) {
//        UserVO result = UserDBManger.findUser(number);
//        return result;
        return UserDBManger.findUser(number);
    }

    // 구글 회원 가입
    public void save(UserVO users) {
        UserDBManger.save(users);
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

    // 문제 푼 총 수량
    public int totalSolved(String userId){
        return UserDBManger.totalSolved(userId);
    }

    // 문재 리스트
    public List<CodeVO> solvedList(PageInfo pi, String userId){
        return UserDBManger.solvedList(pi, userId);
    }

    public void updateStudyId(String userId, String studyId) {
        UserDBManger.updateStudyId(userId, studyId);
    }

    public boolean checkExtensionSync(String userId, String studyId) {
        return UserDBManger.checkExtensionSync(userId,studyId);
    }

    @Transactional
    public int studyGetOut(String userId) {
        // 댓글 삭제
        int result3 = UserDBManger.userIdDeleteComment(userId);
        // 코드 삭제
        int result2 = UserDBManger.userIdDeleteCode(userId);
        // 스터디 탈되로 업데이트
        int result = UserDBManger.studyGetOut(userId);

        return result;
    }
    public void createStudy(String userId, String studyId, Date date) {
        UserDBManger.createStudy(userId, studyId, date);
    }
}
