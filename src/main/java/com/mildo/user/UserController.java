package com.mildo.user;

import com.mildo.user.Auth.JwtTokenProvider;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

//    @GetMapping("/home") // 구글 로그인 성공시 리다이렉트 받는 메서드
//    public ResponseEntity<Map<String, Serializable>> home(@AuthenticationPrincipal OidcUser principal) {
//
//        log.info("principal = {}", principal);
//
//        UserVO user = userService.login(principal);
////        String accessToken = jwtTokenProvider.createAccessToken(user);
////        log.info("accessToken = {}", accessToken);
//
////        Date expiration = getExpirationFromToken(accessToken);
////        log.info("Access Token 만료 시간: {}", expiration);
//
//        return ResponseEntity.ok(Map.of(
////                "expiration", expiration,
////                "accessToken", accessToken,
//                "userId", user.getUserId()
//        ));
//    }

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
        int res = userService.studyGetOut(userId);
        log.info("res = {}", res);
        return res > 0 ? ResponseEntity.ok("삭제 성공") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 실패");
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

//    @GetMapping("/{userId}/google-logout")
//    public String googleLogout(@AuthenticationPrincipal OAuth2User principal, HttpSession session, @PathVariable String userId) {
//        log.info("userId = {}", userId);
//
//        String googleId = principal.getAttribute("sub"); // Google의 고유 사용자 ID
//        log.info("googleId = {}", googleId);
//
//        // Google 로그아웃 URL로 리다이렉트
//        session.invalidate(); // 서버 세션 종료
//        return "OK";
//    }



}
