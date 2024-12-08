package com.mildo.user;

import com.mildo.user.Vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.Map;

import static com.mildo.user.Auth.JwtTokenProvider.getExpirationFromToken;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/")
    public String Page(){
        log.info("aaaaaaaaaaaaaaa");
        return "index";
    }

    @GetMapping("/home") // 구글 로그인 성공시 리다이렉트 받는 메서드
    public RedirectView home(@AuthenticationPrincipal OidcUser principal, HttpServletRequest request) {
        log.info("principal = {}", principal);

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient("google", principal.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        log.info("accessToken = {}", accessToken);

        UserVO user = userService.login(principal);
        log.info("user = {}", user);

        String userId = user.getUserId();
        String social = "google"; // 이러면 social-login/:mildo.com

        String redirectUrl = "http://podofarm.xyz/social-login/" + social + "?userId=" + userId;
        log.info("redirectUrl = {}", redirectUrl);

        return new RedirectView(redirectUrl);
    }

    @GetMapping("/llogin")
    public RedirectView home() {
        log.info("sssssssssss");
        String redirectUrl = "/oauth2/authorization/google";

        return new RedirectView(redirectUrl);
    }


}
