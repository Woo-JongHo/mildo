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
        try{
            session.insert("User.save", users);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback(); // 에러 발생 시 롤백
        } finally {
            session.close(); // 세션 닫기
        }
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
    public static void saveToken(TokenVO token) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("User.saveToken", token);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback(); // 에러 발생 시 롤백
        } finally {
            session.close(); // 세션 닫기
        }
    }

    // 토큰 업데이트
    public static void saveUpdateToken(TokenVO token) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("User.saveUpdateToken", token);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback(); // 에러 발생 시 롤백
        } finally {
            session.close(); // 세션 닫기
        }
    }

    public static String findRefreshTokenByUserId (String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        String Refresh = session.selectOne("User.findRefreshTokenByUserId", userId);
        session.commit();
        session.close();
        return Refresh;
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

    public static boolean checkExtensionSync(String userId, String studyId) {
        boolean isValid = false;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            // 파라미터 맵 생성
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("studyId", studyId);

            Integer count = session.selectOne("User.checkExtensionSync", params);

            if (count != null && count > 0) {
                isValid = true;
            }
        } finally {
            session.close(); // 세션 닫기
        }
        return isValid; // 유효 여부 반환
    }

    public static int studyGetOut(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int res = session.update("User.studyGetOut", userId);
        session.commit();
        session.close();
        return res;
    }

    public static int userIdDeleteCode(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int res = session.update("User.userIdDeleteCode", userId);
        session.commit();
        session.close();
        return res;
    }

    public static int userIdDeleteComment(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int res = session.update("User.userIdDeleteComment", userId);
        session.commit();
        session.close();
        return res;
    }
}
