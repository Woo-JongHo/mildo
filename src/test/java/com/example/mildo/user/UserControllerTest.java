package com.example.mildo.user;

import com.mildo.user.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(UserController.class) // UserController를 테스트한다고 가정
public class UserControllerTest {
    /*
    @Autowired
    private MockMvc mockMvc;

    @Test
    void solvedLevelsId_ShouldReturnBadRequest_WhenUserIdIsInvalid() throws Exception {
        // Given
        String invalidUserId = "%E2%98"; // Invalid percent encoding (incomplete)
        log.info("Test invalidUserId = {}", invalidUserId);

        // When & Then
        mockMvc.perform(get("/api/" + invalidUserId + "/solvedLevels"))
                .andDo(print()) // 로그 출력
                .andExpect(status().isBadRequest()); // Expecting HTTP 400 Bad Request
    }
    */
}
