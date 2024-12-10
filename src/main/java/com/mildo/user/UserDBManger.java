package com.mildo.user;

import com.mildo.db.DBManger;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

@Slf4j
public class UserDBManger extends DBManger {

    public static int signup(UserVO user) {
        int re = -1;
        SqlSession session = sqlSessionFactory.openSession();
        re = session.insert("User.signup", user);
        session.commit();
        session.close();

        return re;
    }
}
