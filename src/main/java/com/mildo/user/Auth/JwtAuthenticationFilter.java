package com.mildo.user.Auth;

import com.mildo.user.UserRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
        if (pathMatcher.match("/api/{userId}/info", requestURI)||
            pathMatcher.match("/api/{userId}/tokenInfo", requestURI) ||
            pathMatcher.match("/", requestURI) ||
            pathMatcher.match("/login/oauth2/code/google", requestURI) ||
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
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                // 토큰이 유효한 경우 추가 작업 가능
                request.setAttribute("user", claims.getSubject());

            } catch (ExpiredJwtException e) { // Access Token 만료 시 발생
                validateRefreshToken(request, response, filterChain);
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

    private void validateRefreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String refreshToken = request.getHeader("RefreshToken");
        log.info("refreshToken = {}", refreshToken);

//        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
        if (refreshToken == null) { // refreshToken이 없을 때
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Refresh Token is missing or invalid");
            return;
        }

        // refreshToken도 앞에 Bearer가 붙는지?
//        refreshToken = refreshToken.substring(7); // "Bearer " 제거
        try {
            // Refresh Token 검증
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // refreshToken이 DB에 있는지 확인
            String userId = claims.getSubject(); // Refresh Token에서 userId 추출
            String storedToken = userRepository.findRefreshTokenByUserId(userId);

            // DB에서 RefreshToken이랑 다르면 해킹 의심
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh Token not found or invalid in DB");
                return;
            }

            // 새로운 Access Token 발급 로직
            String newAccessToken = Jwts.builder()
                    .setSubject(claims.getSubject())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
            log.info("newAccessToken = {}", newAccessToken);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Access Token refreshed successfully");

        } catch (ExpiredJwtException e) { // Refresh Token 만료 시 발생
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Refresh Token has expired");
            return;
        }catch (Exception e) {
            log.error("Exception e = {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Refresh Token");
        }
    }

}
