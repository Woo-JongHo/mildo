package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import com.mildo.db.DBManger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

@Slf4j
public class StudyDBManger extends DBManger {

    public static int create(StudyVO s) {
        int re = -1;
        SqlSession session = sqlSessionFactory.openSession();
        re = session.insert("Study.create", s);
        session.commit();
        session.close();

        return re;
    }

    public static List<StudyVO> studyList(String studyId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> list = session.selectList("Study.studyList", studyId);
        session.commit();
        session.close();

        return list;
    }

    public static int totalMembers(String studyId) {
        SqlSession session = sqlSessionFactory.openSession();
        int totalMembers = session.selectOne("Study.totalMembers", studyId);
        session.commit();
        session.close();

        return totalMembers;
    }

    public static List<StudyVO> studyDays(String studyId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> studyDays = session.selectList("Study.studyDays", studyId);
        session.commit();
        session.close();

        return studyDays;
    }

    public static List<StudyVO> totalrank(String studyId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> totalrank = session.selectList("Study.totalrank", studyId);
        session.commit();
        session.close();

        return totalrank;
    }

    public static String getStartMonth(String studyId){
        SqlSession session = sqlSessionFactory.openSession();
        String getStartMonth = session.selectOne("Study.getStartMonth", studyId);
        session.commit();
        session.close();
        return getStartMonth;

    }


    public static Object getStudyMemberByMonth(String studyId, String month) {
        List<String> names;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("study_Id", studyId);
            params.put("study_start", month);

            names = session.selectList("Study.getStudyMemberByMonth", params);
        } finally {
            session.close();
        }
        return names;
    }

    public static List<String> getStudyMemberIdByMonth(String studyId, String month) {
        List<String> names;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("study_Id", studyId);
            params.put("study_start", month);

            names = session.selectList("Study.getStudyMemberIdByMonth", params);
            System.out.println(names + "스터디원아이디");
        } finally {
            session.close();
        }
        return names;
    }

    public static boolean checkstudyIdPassword(String studyId, String password) {
        boolean isValid = false;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            // 파라미터 맵 생성
            Map<String, Object> params = new HashMap<>();
            params.put("study_Id", studyId);
            params.put("study_password", password);

            Integer count = session.selectOne("Study.checkstudyIdPassword", params);

            if (count != null && count > 0) {
                isValid = true;
            }
        } finally {
            session.close(); // 세션 닫기
        }
        return isValid; // 유효 여부 반환
    }

    public static void enterStudy(String studyId, String password, String userId) {


    }

    public static StudyVO updateStudyName(String studyCode, String studyName) {
        SqlSession session = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>();
        params.put("study_Code", studyCode);
        params.put("study_Name", studyName);
        StudyVO updateStudyName = session.selectOne("User.updateStudyName", params);
        session.commit();
        session.close();
        return updateStudyName;
    }
}
