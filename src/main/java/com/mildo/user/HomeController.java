package com.mildo.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        log.info("aaaaaaaaaaaaaaa");
        return "index";
    }

    @GetMapping("/google-logout")
    public String googleLogout() {
        // Google 로그아웃 URL로 리다이렉트
        return "redirect:https://accounts.google.com/logout";
    }
}
