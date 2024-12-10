package com.mildo.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String home(){
        return "Home2@@!!";
    }

    @PostMapping("/login")
    public String login(String name, String googleId, String email){
        log.info("[UserController] login : name={}, email={}, googleId={}", name, googleId, email);
        userService.login(name, googleId, email);

        return "Login Succeed!";
    }

}
