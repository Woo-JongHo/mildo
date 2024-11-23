package com.mildo.user;

import com.mildo.user.Auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.mildo.user.Auth.JwtTokenProvider.getExpirationFromToken;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 클래스

    @GetMapping("/home") // 구글 로그인 성공시 리다이렉트 받는 메서드
    public ResponseEntity<Map<String, Serializable>> home(@AuthenticationPrincipal OidcUser principal) {

        UserVO user = userService.login(principal);
        String accessToken = jwtTokenProvider.createAccessToken(user);
        log.info("accessToken = {}", accessToken);

        Date expiration = getExpirationFromToken(accessToken);
        log.info("Access Token 만료 시간: {}", expiration);

        return ResponseEntity.ok(Map.of(
                "expiration", expiration,
                "accessToken", accessToken,
                "userId", user.getUserId()
        ));
    }

    @PostMapping("/google-login2")
    public String googleLogin2(@RequestParam("userNumber") String userNumber) {
        log.info("user = {}", userNumber);

        return "OK";
    }

}
