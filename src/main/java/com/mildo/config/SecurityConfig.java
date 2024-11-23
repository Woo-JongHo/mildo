package com.mildo.config;

import com.mildo.user.Auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/google-login", "/google-login2").permitAll()
//                        .requestMatchers("/", "/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        http
                .oauth2Login((oauth2) -> oauth2
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동할 URL
                );

        http
                .logout((auth) -> auth
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/google-logout")  // 로그아웃 성공 시 이동할 페이지
                .invalidateHttpSession(true)  // 세션 무효화
                .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
        );

        http
                .csrf((auth) -> auth.disable()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 필터 등록
                );

        return http.build();
    }

}
