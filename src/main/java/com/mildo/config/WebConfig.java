package com.mildo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://school.programmers.co.kr", "https://www.podofarm.xyz", "https://podofarm.xyz",
                        "http://mildo.xyz","https://mildo.xyz",
                        "http://localhost:5173", "http://localhost:5174", "http://localhost:5175") // 허용할 도메인 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("https://podofarm.xyz/"); // 프론트엔드 도메인
//        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
//        config.addAllowedHeader("*"); // 모든 헤더 허용
//        config.setAllowCredentials(true); // 쿠키 포함 요청 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

}
