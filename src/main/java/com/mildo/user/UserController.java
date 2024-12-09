package com.mildo.user;

import com.mildo.study.Vo.StudyVO;
import com.mildo.user.Auth.JwtTokenProvider;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 클래스

    @GetMapping("/logoutSucc") // 로그인 성공시 리다이렉트
    public String logout( ) {
        // 토큰 만료는 서버 쪽에서 처리
        return "OKAY22222222";
    }

    // userId로 회원 조회 | 요청 방법:/api/%23G909/info
    @ResponseBody
    @GetMapping(value = "/{userId}/info", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserVO> userInfo(@PathVariable String userId) {
        log.info("userId = {}", userId);
        UserVO user = userService.finduserId(userId);
        log.info("user = {}", user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @ResponseBody
    @GetMapping(value="/{userId}/tokenInfo", produces="application/json; charset=UTF-8")
    public ResponseEntity<TokenVO> tokenInfo(@PathVariable String userId){
        userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
        TokenVO token = userService.makeToken(userId);

        if (token == null || token.getRefreshToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 상태코드를 401 로 보냄
        }
        return ResponseEntity.ok(token);
    }

    // userId로 레벨 별 푼 문제 카운트 조회 | 요청 방법:/api/%23G909/solvedLevels
    @ResponseBody
    @GetMapping(value="/{userId}/solvedLevels", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<LevelCountDTO>> solvedLevelsId(@PathVariable String userId){
        List<LevelCountDTO> solvedLevels = userService.solvedLevelsList(userId);
        return ResponseEntity.ok(solvedLevels);
    }

    @ResponseBody // userId로 스터디 탈퇴
    @GetMapping(value="/{userId}/studyOut", produces="application/json; charset=UTF-8")
    public ResponseEntity<String> studyOut(@PathVariable String userId){
        log.info("userId = {}", userId);
        UserVO user = userService.finduserId(userId);

        if("Y".equals(user.getUserLeader())){ // 리더인지 확인
            return ResponseEntity.ok("리더는 탈퇴 위임하고 해");
        } else {
            int res = userService.studyGetOut(userId);
            log.info("res = {}", res);
            return res > 0 ? ResponseEntity.ok("탈퇴 성공") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴 실패");
        }
    }

    @GetMapping("/login-failed")
    public String faillLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        log.info("session = {}", session.getId());

        return "FAILL";
    }

    @ResponseBody
    @GetMapping(value="/{userId}/userTotalSolved", produces="application/json; charset=UTF-8")
    public Map<String, Object> userTotalSolved(@PathVariable String userId){
        int res = userService.userTotalSolved(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("user_solvedproblem", res);
        return response;
    }

    @ResponseBody // userId로 회원 탈퇴
    @GetMapping(value="/{userId}/service-out", produces="application/json; charset=UTF-8")
    public ResponseEntity<String> serviceOut(@PathVariable String userId){
        log.info("userId = {}", userId);
        int res = userService.serviceOut(userId);
        log.info("res = {}", res);
        return res > 0 ? ResponseEntity.ok("탈퇴 성공") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴 실패");
    }

    @GetMapping("/{userId}/google-logout")
    public ResponseEntity<String> googleLogout(@PathVariable String userId, HttpServletRequest request) {
        log.info("userId = {}", userId);

        log.info("request = {}", request.getSession());
        // 세션 무효화
        request.getSession().invalidate();

        String result = userService.blackToken(userId);
        return "토큰이 없음".equals(result) ? ResponseEntity.ok("토큰은 없지만 로그아웃 성공") : ResponseEntity.ok("로그아웃 성공");
    }

    @ResponseBody
    @PatchMapping(value = "/{userId}", produces="application/json; charset=UTF-8")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserVO vo) {
        int res = userService.changUserInfo(userId, vo);
        return res > 0 ? ResponseEntity.ok("변경 성공") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("변경 실패");
    }

}
