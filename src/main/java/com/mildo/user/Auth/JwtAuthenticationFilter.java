package com.mildo.user.Auth;

import com.mildo.user.UserRepository;
import com.mildo.user.Vo.BlackTokenVO;
import com.mildo.user.Vo.TokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String SECRET_KEY = JwtTokenProvider.SECRET_KEY;

    private final UserRepository userRepository;

    @Autowired
    public JwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        // AntPathMatcher 으로 쉽게 동적 경로를 처리할 수 있다.
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // 인증이 필요 없는 URL
        if (pathMatcher.match("/user/{userId}/info", requestURI)||
            pathMatcher.match("/user/{userId}/tokenInfo", requestURI) ||
            pathMatcher.match("/", requestURI) ||
            pathMatcher.match("/login/oauth2/code/google", requestURI) ||
            pathMatcher.match("/auth/refresh", requestURI) ||
            pathMatcher.match("/new-token", requestURI) ||
            pathMatcher.match("/new-token2", requestURI) ||
            pathMatcher.match("/llogin", requestURI) ||
            pathMatcher.match("/home", requestURI) ||
            pathMatcher.match("/login-failed", requestURI) ||
            requestURI.startsWith("/public")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access Token is missing");
            return;
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 부분 제거
            try {
                // 토큰 검증 및 만료 시간 확인

                log.info("token = {}", token);
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                log.info("claims = {}", claims);

                BlackTokenVO tokenVo = userRepository.checkBlackList(token); // 블랙리스트에 있는 토큰인지 검사
                if(tokenVo != null){ // 블랙리스트에 토큰이 있으면 보안이 위험
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Danger Security Token!!");
                    return;
                }

                // 토큰이 유효한 경우 추가 작업 가능
                request.setAttribute("user", claims.getSubject());

            } catch (ExpiredJwtException e) { // Access Token 만료 시 발생
                response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
//                Claims expiredClaims = e.getClaims(); // 만료된 토큰의 Claims 가져오기
//                validateRefreshToken(request, response, filterChain, expiredClaims);
                return;

            }catch (Exception e) {
                log.error("Exception e = {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Invalid Token");
                return;
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

//    public void validateRefreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Claims expiredClaims) throws IOException {
//
//        log.info("expiredClaims = {}", expiredClaims);
//        log.info("getSubject = {}", expiredClaims.getSubject());
//        TokenVO Ref = userRepository.Refresh(expiredClaims.getSubject()); // 유저 아이디로 토큰이 있나 확인
//
//        log.info("Ref = {}", Ref);
//        if(Ref != null){ // 토큰이 유효하면
//            // 새 Access Token 발급
//            String newAccessToken = Jwts.builder()
//                    .setSubject(expiredClaims.getSubject())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
//                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                    .compact();
//
//            log.info("newAccessToken = {}", newAccessToken);
//
//            // HTTP 응답 헤더에 새 Access Token 설정
//            response.setHeader("Authorization", "Bearer " + newAccessToken);
//            response.getWriter().write("New Access Token");
//            return;
//        }
//
//        // Refresh Token이 유효하지 않으면 에러 응답
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Invalid or missing Refresh Token");
//    }

}
