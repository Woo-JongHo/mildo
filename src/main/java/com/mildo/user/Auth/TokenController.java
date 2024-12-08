package com.mildo.user.Auth;

import com.mildo.user.UserRepository;
import com.mildo.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private static final String REFRESH_SECRET_KEY = JwtTokenProvider.REFRESH_TOKEN_SECRET_KEY;

    private final UserService userService;

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log.info("date = {}", timestamp);
        userService.blackrest(timestamp); // 블랙 리스트 초기화

        try {
            if (!refreshToken.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Refresh Token format.");
            }
            refreshToken = refreshToken.substring(7);
            log.info("refreshToken = {}", refreshToken);

            // Refresh Token 검증
            Claims claims = Jwts.parser()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String userId = claims.getSubject();
            String storedToken = userService.findRefreshTokenByUserId(userId);

            if (storedToken == null || !storedToken.equals(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Refresh Token.");
            }

            // 새 Access Token 발급
            String newAccessToken = Jwts.builder()
                    .setSubject(userId)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
                    .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET_KEY)
                    .compact();

            log.info("newAccessToken = {}", newAccessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + newAccessToken);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("Access Token refreshed successfully = " + newAccessToken);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh Token has expired.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Refresh Token.");
        }
    }


}
