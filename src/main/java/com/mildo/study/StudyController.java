package com.mildo.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {
    private final StudyService studyService;

    @GetMapping
    public String index() {
        log.info("index");
        return "Study Home";
    }

    @PostMapping("/create")
    public String create(String name, String password) {

        studyService.create(name, password);

        return "Create Study";
    }

}
