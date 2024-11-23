package com.mildo.code;

import com.mildo.db.DBManger;
import org.apache.ibatis.session.SqlSession;

public class CodeDBManger extends DBManger {

    public static int dummyCode(String userId) {
        int re = -1;
        SqlSession session = sqlSessionFactory.openSession();
        re = session.insert("code.dummyCode", userId);
        session.commit();
        session.close();

        return re;
    }
}
