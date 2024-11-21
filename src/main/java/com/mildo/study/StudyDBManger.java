package com.mildo.study;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import com.mildo.db.DBManger;

@Slf4j
public class StudyDBManger extends DBManger {

    public static int create(StudyVO s) {
        log.info("StudyDBManger: study create {}", s);
        int re = -1;
        if (sqlSessionFactory == null) {
            log.error("StudyDBManger: session is null");
        } else {
            SqlSession session = sqlSessionFactory.openSession();
            re = session.insert("Study.create", s);
            session.commit();
            session.close();
        }
        return re;
    }
}
