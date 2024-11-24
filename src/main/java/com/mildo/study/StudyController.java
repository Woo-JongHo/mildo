package com.mildo.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // studyCode로 스터디 리스트 가져오기 | 호출 방법 /api/%23E3R4/memberList
    @GetMapping("/api/{studyCode}/memberList")
    public Map<String, Object> studyList(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        log.info("studyCode = {}", studyCode);

        List<StudyVO> list = studyService.studyList(studyCode);
        int totalMembers = studyService.totalMembers(studyCode);
        log.info("list = {}", list);

        Map<String, Object> response = new HashMap<>();
        response.put("members", list);
        response.put("totalMembers", totalMembers);

        return response;
    }



}
