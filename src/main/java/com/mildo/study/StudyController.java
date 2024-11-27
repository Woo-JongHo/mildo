package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/study")
public class StudyController {
    private final StudyService studyService;

    @GetMapping
    public String index() {
        log.info("index");
        return "Study Home";
    }

    @PostMapping("/create")
    public String create(String userId, String name, String password) {
        // 유저 아이디 검증 (존재하는 아이디인지 + 토큰 검증)

        studyService.create(userId, name, password);

        return "Create Study";
    }

    // studyCode로 스터디 리스트 가져오기 | 호출 방법 /api/%23E3R4/memberList
    @GetMapping("/{studyCode}/memberList")
    public List<StudyVO> studyList(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        return studyService.studyList(studyCode); // 멤버 리스트
    }

    // 멤버 수
    @GetMapping("/{studyCode}/membersCount")
    public Map<String, Object> membersCount(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        int totalMembers = studyService.totalMembers(studyCode);  // 멤버 수

        Map<String, Object> response = new HashMap<>();
        response.put("totalMembers", totalMembers);

        return response;
    }

    // 남은 일수, 진행 한 수
    @GetMapping("/{studyCode}/studyDays")
    public List<StudyVO> studyDay(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        return studyService.studyDays(studyCode);
    }

    // studyCode로 스터디 등수 가져오기 | 호출 방법 /api/%23E3R4/rank
    @GetMapping("/{studyCode}/rank")
    public Map<String, Object> ranks(@PathVariable String studyCode) {
        studyCode = URLDecoder.decode(studyCode, StandardCharsets.UTF_8);
        log.info("studyCode = {}", studyCode);

        List<StudyVO> totalrank = studyService.totalrank(studyCode);

        Map<String, Object> response = new HashMap<>();
        response.put("totalrank", totalrank);

        return response;
    }

    //스터디 참가하기
    @PutMapping("/enterStudy")
    public ResponseEntity<String> enterStudy(@RequestParam String studyCode,
                                             @RequestParam String password,
                                             @RequestParam String userId) {
        boolean isValid = studyService.checkStudyCodePassword(studyCode, password);

        if (isValid) {
            studyService.enterStudy(studyCode,password,userId);
            return ResponseEntity.ok("스터디 접속 완료"); // 200 OK 응답
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid study code or password!");
        }
    }
}
