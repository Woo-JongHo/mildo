package com.swyp.mildo.code;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
public class CodeController {
    private final CodeService codeService;

    @GetMapping
    public String code() {
        return "Code is running";
    }
}
