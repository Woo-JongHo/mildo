package com.mildo.user;

import com.mildo.user.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@WebMvcTest(UserController.class) // UserController를 테스트한다고 가정
public class UserTest {

    @Test
    void testSuccessLogging() {
        log.info("테스트 성공!");

        int expected = 1;
        int actual = 1;
        assert expected == actual : "테스트 실패";
    }

    @Test
    void decode_ShouldReturnDecodedString_WhenEncodedStringIsValid() {
        // Given
        String encoded = "%E2%98%83"; // Encoded version of "☃"

        // When
        String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8);

        // Then
        assertEquals("☃", decoded);
    }



}
