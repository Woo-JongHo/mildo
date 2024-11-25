package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String create(String userId, String name, String password) {

        studyService.create(name, password);
        studyService.create(userId, name, password);

        return "Create Study";
    }

    // studyCode로 스터디 리스트 가져오기 | 호출 방법 /api/%23E3R4/memberList
    @GetMapping("/api/{studyCode}/memberList")
    public Map<String, Object> studyList(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        log.info("studyCode = {}", studyCode);

        List<StudyVO> studyList = studyService.studyList(studyCode); // 멤버 리스트
        int totalMembers = studyService.totalMembers(studyCode);  // 멤버 수
        List<StudyVO> studyDays = studyService.studyDays(studyCode); // 남은 일수, 진행 한 수

        Map<String, Object> response = new HashMap<>();
        response.put("studyList", studyList);
        response.put("totalMembers", totalMembers);
        response.put("studyDays", studyDays);

        return response;
    }

    // studyCode로 스터디 등수 가져오기 | 호출 방법 /api/%23E3R4/rank
    @GetMapping("/api/{studyCode}/rank")
    public Map<String, Object> ranks(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        log.info("studyCode = {}", studyCode);

        List<StudyVO> totalrank = studyService.totalrank(studyCode);

        Map<String, Object> response = new HashMap<>();
        response.put("totalrank", totalrank);

        return response;
    }

}
