package com.mildo.study;

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

}
