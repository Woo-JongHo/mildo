package com.mildo.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    // 구글 로그인 회원 조회
    public UserVO findUser(String number) {
        UserVO result = UserDBManger.findUser(number);
        return result;
    }

    // 구글 회원 가입
    public void save(UserVO users) {
        UserDBManger.save(users);

    }


}
