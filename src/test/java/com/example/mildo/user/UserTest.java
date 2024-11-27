package com.example.mildo.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
public class UserTest {

    @Test
    void testSuccessLogging() {
        // 로그를 찍기 전에 테스트가 성공적으로 실행된다는 걸 확인
        log.info("테스트 성공!");

        int expected = 1;
        int actual = 1;
        assert expected == actual : "테스트 실패";
    }


}
