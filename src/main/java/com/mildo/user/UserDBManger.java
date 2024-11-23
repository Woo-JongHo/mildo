package com.mildo.user;

import com.mildo.db.DBManger;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

@Slf4j
public class UserDBManger extends DBManger {

    // 구글 로그인 회원 조회
    public static UserVO findUser(String number) {
        log.info("number = {}", number);
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


}
