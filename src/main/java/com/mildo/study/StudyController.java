package com.mildo.study;

import com.mildo.study.Vo.StudyVO;
import com.mildo.user.UserService;
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
    private final UserService userService;

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

    // studyId로 스터디 리스트 가져오기 | 호출 방법 /api/%23E3R4/memberList
    @GetMapping("/{studyId}/memberList")
    public List<StudyVO> studyList(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        return studyService.studyList(studyId); // 멤버 리스트
    }

    // 멤버 수
    @GetMapping("/{studyId}/membersCount")
    public Map<String, Object> membersCount(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        int totalMembers = studyService.totalMembers(studyId);  // 멤버 수

        Map<String, Object> response = new HashMap<>();
        response.put("totalMembers", totalMembers);

        return response;
    }

    // 남은 일수, 진행 한 수
    @GetMapping("/{studyId}/studyDays")
    public List<StudyVO> studyDay(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        return studyService.studyDays(studyId);
    }

    // studyId로 스터디 등수 가져오기 | 호출 방법 /api/%23E3R4/rank
    @GetMapping("/{studyId}/rank")
    public Map<String, Object> ranks(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        log.info("studyId = {}", studyId);

        List<StudyVO> totalrank = studyService.totalrank(studyId);

        Map<String, Object> response = new HashMap<>();
        response.put("totalrank", totalrank);

        return response;
    }

    //스터디 참가하기
    @RequestMapping(value = "/enterStudy", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<String> enterStudy(@RequestParam(defaultValue = "423XDF") String studyId,
                                             @RequestParam(defaultValue = "1111")String password ,
                                             @RequestParam(defaultValue = "#3231") String userId) {
        boolean isValid = studyService.checkstudyIdPassword(studyId, password);

        if (isValid) {
            userService.updateStudyId(userId, studyId);
            return ResponseEntity.ok("스터디 접속 완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid study code or password!");
        }
    }
}
