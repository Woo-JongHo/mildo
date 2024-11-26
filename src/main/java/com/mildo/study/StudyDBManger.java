package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import com.mildo.db.DBManger;

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

    public static List<StudyVO> studyList(String studyCode) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> list = session.selectList("Study.studyList", studyCode);
        session.commit();
        session.close();

        return list;
    }

    public static int totalMembers(String studyCode) {
        SqlSession session = sqlSessionFactory.openSession();
        int totalMembers = session.selectOne("Study.totalMembers", studyCode);
        session.commit();
        session.close();

        return totalMembers;
    }

    public static List<StudyVO> studyDays(String studyCode) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> studyDays = session.selectList("Study.studyDays", studyCode);
        session.commit();
        session.close();

        return studyDays;
    }

    public static List<StudyVO> totalrank(String studyCode) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StudyVO> totalrank = session.selectList("Study.totalrank", studyCode);
        session.commit();
        session.close();

        return totalrank;
    }

    public static String getStartMonth(String studyCode){
        SqlSession session = sqlSessionFactory.openSession();
        String getStartMonth = session.selectOne("Study.getStartMonth", studyCode);
        session.commit();
        session.close();
        return getStartMonth;

    }


}
