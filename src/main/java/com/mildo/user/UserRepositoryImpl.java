package com.mildo.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl  implements UserRepository {
    @Override
    public void signup(UserVO user) {
        log.info("[UserRepositoryImpl] user : {}", user);

        int result = UserDBManger.signup(user);

        log.info("signup result:{}", result);
    }
}
