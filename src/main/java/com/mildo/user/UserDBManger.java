package com.mildo.user;

import com.mildo.code.CodeVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.db.DBManger;
import com.mildo.study.Vo.StudyVO;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserDBManger extends DBManger {

    // 구글 로그인 회원 조회
    public static UserVO findUser(String number) {
        SqlSession session = sqlSessionFactory.openSession();
        UserVO re = session.selectOne("User.findUser", number);
        log.info("re = {}", re);
        session.commit();
        session.close();

        return re;
    }

    // 구글 회원 가입
    public static void save(UserVO users) {
        SqlSession session = sqlSessionFactory.openSession();
        session.insert("User.save", users);
        session.commit();
        session.close();
    }

    // userId로 회원 조회
    public static UserVO finduserId(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        UserVO user = session.selectOne("User.finduserId", userId);
        session.commit();
        session.close();

        return user;
    }

    // userId로 토큰 조회
    public static TokenVO findToken(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        TokenVO findToken = session.selectOne("User.findToken", userId);
        session.commit();
        session.close();

        return findToken;
    }

    // 토큰 생성후 값 넣기
    public static void saveToken(TokenVO tkoen) {
        SqlSession session = sqlSessionFactory.openSession();
        session.insert("User.saveToken", tkoen);
        session.commit();
        session.close();
    }

    // 코드 레벨별로 갯수 가져오기
    public static List<LevelCountDTO> solvedLevelsList(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<LevelCountDTO> solvedLevels = session.selectList("User.solvedLevelsList", userId);
        session.commit();
        session.close();
        return solvedLevels;
    }

    // 문제 푼 총 수량
    public static int totalSolved(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int totalSolved = session.selectOne("User.totalSolved", userId);
        session.commit();
        session.close();
        return totalSolved;
    }

    // 문재 리스트
    public static List<CodeVO> solvedList(PageInfo pi, String userId) {
        SqlSession session = sqlSessionFactory.openSession();

        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        int limit = pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, limit);

        List<CodeVO> solvedList = session.selectList("User.solvedList", userId, rowBounds);

        session.commit();
        session.close();
        return solvedList;
    }

    public static void updateStudyId(String userId, String studyId) {
        SqlSession session = sqlSessionFactory.openSession();
        log.info(userId + "userId-----" + studyId + "--------studyId");
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            params.put("studyId", studyId);

            session.update("User.updateStudyId", params);
            session.commit(); // 변경 사항 적용
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback(); // 에러 발생 시 롤백
        } finally {
            session.close(); // 세션 닫기
        }
    }
}
