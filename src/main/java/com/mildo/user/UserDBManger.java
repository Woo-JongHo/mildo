package com.mildo.user;

import com.mildo.db.DBManger;
import com.mildo.study.Vo.EnteStudy;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserDBManger extends DBManger {

    // 구글 로그인 회원 조회
    public static UserVO findUser(String number) {
        SqlSession session = sqlSessionFactory.openSession();
        UserVO re = session.selectOne("User.findUser", number);
        session.commit();
        session.close();

        return re;
    }

    // 회원 아이디 가져오기
    public static String findNullUserId() {
        SqlSession session = sqlSessionFactory.openSession();
        String userId = session.selectOne("User.findNullUserId");
        session.commit();
        session.close();

        return userId;
    }

    // 구글 회원 가입 업데이트
    public static void saveUpdateUser(UserVO users) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("User.saveUpdateUser", users);
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

    public static void updateStudyId(EnteStudy enteStudy) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
//            Map<String, String> params = new HashMap<>();
//            params.put("userId", userId);
//            params.put("studyId", studyId);

            session.update("User.updateStudyId", enteStudy);
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

    public static int userIdChangNull(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int res = session.update("User.userIdChangNull", userId);
        session.commit();
        session.close();
        return res;
    }

    public static int createStudy(String userId, String studyId, Date date) {
        SqlSession session = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("studyId", studyId);
        // params.put("participant", date);

        int res = session.update("User.createStudy", params);
        session.commit();
        session.close();
        return res;
    }

    public static int userServiceOut(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        int res = session.delete("User.userServiceOut", userId);
        session.commit();
        session.close();
        return res;
    }
}
