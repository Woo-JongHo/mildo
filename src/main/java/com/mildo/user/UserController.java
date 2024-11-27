package com.mildo.user;

import com.mildo.code.CodeVO;
import com.mildo.user.Auth.JwtTokenProvider;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mildo.user.Auth.JwtTokenProvider.getExpirationFromToken;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 클래스

//    @GetMapping("/home") // 구글 로그인 성공시 리다이렉트 받는 메서드
//    public ResponseEntity<Map<String, Serializable>> home(@AuthenticationPrincipal OidcUser principal) {
//
//        log.info("principal = {}", principal);
//
//        UserVO user = userService.login(principal);
//        String accessToken = jwtTokenProvider.createAccessToken(user);
//        log.info("accessToken = {}", accessToken);
//
////        Date expiration = getExpirationFromToken(accessToken);
////        log.info("Access Token 만료 시간: {}", expiration);
//
//        return ResponseEntity.ok(Map.of(
////                "expiration", expiration,
//                "accessToken", accessToken,
//                "userId", user.getUserId()
//        ));
//    }

    @GetMapping("/logoutSucc") // 로그인 성공시 리다이렉트
    public String logout( ) {
        // 토큰 만료는 서버 쪽에서 처리
        return "OKAY22222222";
    }

    // userId로 회원 조회 | 요청 방법:/api/%23G909/info
    @GetMapping("/api/{userId}/info")
    public UserVO userInfo(@PathVariable String userId){
        userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
        return userService.finduserId(userId);
    }

    @GetMapping("/api/{userId}/tokenInfo")
    public Map<String, Object> tokenInfo(@PathVariable String userId){
        userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
//        TokenVO token = userService.saveToken(userId); // DB에 토큰 저장 할꺼면 사용
        TokenVO token = userService.makeToken(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    // userId로 레벨 별 푼 문제 카운트 조회 | 요청 방법:/api/%23G909/solvedLevels
    @GetMapping("/api/{userId}/solvedLevels")
    public ResponseEntity<List<LevelCountDTO>> solvedLevelsId(@PathVariable String userId){
        try {
            // 잘못된 인코딩 처리
            userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.error("encoding error : {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request 반환
        }

        List<LevelCountDTO> solvedLevels = userService.solvedLevelsList(userId);

        if (solvedLevels == null || solvedLevels.isEmpty()) {
            // 상태코드를 404 로 보내고 본문에 null이라고 보냄 비워도 됨!
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(solvedLevels);
    }

    // userId로 푼 문제 리스트 조회 | 요청 방법:/api/%23G909/solvedList
    @GetMapping("/api/{userId}/solvedList")
    public List<CodeVO> solvedListId(@PathVariable String userId){
        userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
        List<CodeVO> solvedList = userService.solvedList(userId);

        return solvedList;
    }

}
