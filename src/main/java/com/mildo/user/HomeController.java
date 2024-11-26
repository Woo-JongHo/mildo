package com.mildo.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.mildo.user.Vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.Map;

import static com.mildo.user.Auth.JwtTokenProvider.getExpirationFromToken;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
    public String Page(){
        log.info("aaaaaaaaaaaaaaa");
        return "index";
    }

    @GetMapping("/google-logout")
    public String googleLogout() {
        // Google 로그아웃 URL로 리다이렉트
        return "redirect:https://accounts.google.com/logout";
    }

    @GetMapping("/home") // 구글 로그인 성공시 리다이렉트 받는 메서드
    public RedirectView home(@AuthenticationPrincipal OidcUser principal) {
        log.info("principal = {}", principal);
        String userId = "#2442";
        String social = "google"; // 이러면 social-login/:mildo.com
        String redirectUrl = "/social-login/" + social + "?userId=" + userId;

//        예상 도메인
//        String redirectUrl = "https://mildo.com/social-login/" + social + "?userId=" + userId;

        return new RedirectView(redirectUrl);
    }


}
