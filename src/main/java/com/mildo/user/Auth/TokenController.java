package com.mildo.user.Auth;

import com.mildo.user.UserRepository;
import com.mildo.user.UserService;
import com.mildo.user.Vo.AccessVO;
import com.mildo.user.Vo.TokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private static final String REFRESH_SECRET_KEY = JwtTokenProvider.REFRESH_TOKEN_SECRET_KEY;
    private static final String SECRET_KEY = JwtTokenProvider.SECRET_KEY;

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ResponseBody
    @GetMapping(value="/new-token2", produces="application/json; charset=UTF-8")
    public ResponseEntity<?> getCookieValue(@CookieValue(name = "RefreshToken", required = false) String RefreshToken, HttpServletRequest request) {
        log.info("RefreshToken = {}", RefreshToken);

        if (RefreshToken == null) { // 쿠키에 토큰이 없음
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token is missing");
        }

        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .parseClaimsJws(RefreshToken)
                    .getBody();


            TokenVO isRefresh = userService.findRefreshTokenByUserId(RefreshToken); // DB 확인
            if(isRefresh == null){ // 토큰이 DB에 없으면
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Danger Security Token!!");
            }

            // 아무런 문제 없으면 토큰 만들어 주기
            String newToken = newcreateAccessToken(claims);
            // DB에 새로운 토큰 저장
            AccessVO access = userService.updateNewToken(newToken, claims.getSubject());
            return ResponseEntity.ok(access);


        } catch (ExpiredJwtException e) { // Token 만료 시 발생
            log.error("ExpiredJwtException e = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리플 만료 재로그인 바람");
        }catch (Exception e) { // 유효하지 않으면
            log.error("Exception e = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

    @ResponseBody
    @GetMapping(value="/check-token", produces="application/json; charset=UTF-8")
    public boolean isRefreshTokenExpired(@CookieValue(name = "RefreshToken", required = false) String RefreshToken) {
        if (RefreshToken == null || RefreshToken.isEmpty()) {
            return false;
        }

        try{
            Date expiration = jwtTokenProvider.getExpirationFromRefreshToken(RefreshToken);
            Timestamp now = new Timestamp(System.currentTimeMillis());

            return expiration.after(now); // 만료 시간과 현재 시간 비교
        } catch (ExpiredJwtException e) { // Token 만료 시 발생
            log.error("ExpiredJwtException e = {}", e.getMessage());
            return false;
        }catch (Exception e) { // 유효하지 않으면
            log.error("Exception e = {}", e.getMessage());
            return false;
        }
    }

    public String newcreateAccessToken(Claims expiredClaims) {
        // 새 Access Token 발급
        String newAccessToken = Jwts.builder()
                .setSubject(expiredClaims.getSubject())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return newAccessToken;
    }





//    @ResponseBody
//    @GetMapping(value="/new-token", produces="application/json; charset=UTF-8")
//    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) throws IOException {
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        userService.blackrest(timestamp); // 블랙 리스트 초기화
//
//        String token = request.getHeader("Authorization");
//        token = token.substring(7);
//
//        if (token == null) { // 헤더에 토큰이 없음
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Token is missing");
//        }
//
//        try{
//            Claims claims = Jwts.parser()
//            .setSigningKey(SECRET_KEY)
//            .parseClaimsJws(token)
//            .getBody();
//
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("아직 토큰 유효한데?");
//
//        } catch (ExpiredJwtException e){ // 만료된 토큰이 오게 되있음
//            Claims expiredClaims = e.getClaims(); // 만료된 토큰의 Claims 가져오기
//            String newToken = validateRefreshToken(expiredClaims);
//            return newToken != null ? ResponseEntity.status(HttpStatus.OK).body(newToken) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token Expired 로그인 재시도");
//
//        }catch (Exception e) { // 유효하지 않으면
//            log.error("Exception e = {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid Token");
//        }
//    }
//
//    public String validateRefreshToken(Claims expiredClaims) {
//
//        TokenVO Ref = userRepository.Refresh(expiredClaims.getSubject()); // 유저 아이디로 토큰이 있나 확인
//
//        if(Ref != null){ // 토큰이 유효하면
//            // 새 Access Token 발급
//            String newAccessToken = Jwts.builder()
//                    .setSubject(expiredClaims.getSubject())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
//                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                    .compact();
//
//            return newAccessToken;
//        }
//        return null;
//    }

}
