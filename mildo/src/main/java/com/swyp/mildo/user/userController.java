package com.swyp.mildo.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {

    @GetMapping
    public String home(){
        return "Home";
    }
}
