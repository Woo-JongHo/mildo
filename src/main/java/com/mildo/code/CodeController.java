package com.mildo.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/dummyCode")
    public String login(String userId){
        log.info("userId = {}", userId);
        codeService.dummyCode(userId);

        return "Login Succeed!";
    }

    
}
