package com.mildo.study;

import com.mildo.study.Vo.RemainingDaysDTO;
import com.mildo.study.Vo.StudyVO;
import com.mildo.user.UserService;
import com.mildo.user.Vo.UserVO;
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
@RequestMapping("/study")
public class StudyController {
    private final StudyService studyService;
    private final UserService userService;

    @GetMapping
    public String index() {
        log.info("index");
        return "Study Home";
    }

    @PostMapping("/create")
    public Map<String, Object> create(String userId, String name, String password) {
        // 유저 아이디 검증 (존재하는 아이디인지 + 토큰 검증)
        Map<String, Object> response = new HashMap<>();
        UserVO userVO = userService.finduserId(userId);

        if(userVO.getStudyId() != null) { // 유저 studyId가 있으면 null 반납
            response.put("studyId", null);
            return response;
        }

        String studyId = studyService.create(userId, name, password);
        response.put("studyId", studyId);
        return response;
    }

    // studyId로 스터디 리스트 가져오기 | 호출 방법 /api/%23E3R4/memberList
    @GetMapping("/{studyId}/memberList")
    public List<StudyVO> studyList(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        log.info("studyId = {}", studyId);
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
    public RemainingDaysDTO studyDay(@PathVariable String studyId) {
        studyId = URLDecoder.decode(studyId, StandardCharsets.UTF_8);
        log.info("studyId = {}", studyId);

        return studyService.studyDays(studyId);
    }
    @GetMapping("/{studyId}/studyName")
    public String getStudyName(@PathVariable String studyId) {

        return studyService.getStudyName(studyId);
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
    public ResponseEntity<String> enterStudy(@RequestParam String studyId,
                                             @RequestParam String password ,
                                             @RequestParam String userId) {
        boolean isValid = studyService.checkstudyIdPassword(studyId, password);

        if (isValid) {
            userService.updateStudyId(userId, studyId);
            return ResponseEntity.ok("스터디 접속 완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid study code or password!");
        }
    }

    @GetMapping("{studyId}/monthData")
    public Map<String, Map<String,List<String>>> monthData(@PathVariable String studyId){

        log.info("studyId 받아오는가" + studyId);

        return studyService.Mildo(studyId);
    }

    // 스터디 이름 바꾸기 구현
    @PutMapping("/{studyId}/rename")
    public ResponseEntity<List<StudyVO>> updateUser(@PathVariable String studyId, String studyName) {
        List<StudyVO> updatedUser = studyService.updateStudyName(studyId, studyName);
        log.info("updatedUser = {}", updatedUser);

        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        }
        return ResponseEntity.ok(updatedUser);
    }

    // 스터디 리더 변경 기능 구현
    @PutMapping("/{studyId}/update-leader")
    public ResponseEntity<String> updateLeader(@PathVariable String studyId, @RequestBody Map<String, String> requestBody){
        String newLeaderId = requestBody.get("newLeaderId");
        log.info("newLeaderId = {}", newLeaderId);

        String leaderId = studyService.updateLeader(studyId, newLeaderId);

        if (leaderId.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404

        return ResponseEntity.ok(newLeaderId);
    }

    // 스터디 삭제 기능 구현
    @DeleteMapping("/{studyId}/delete-study")
    public ResponseEntity<Boolean> deleteStudy(@PathVariable String studyId){
        // 유저 아이디를 받아 해당 유저가 리더가 맞는지 판단하는 것도 좋을 것같음

        boolean isValid =  studyService.deleteStudy(studyId);
        if (isValid)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404

        return ResponseEntity.ok(true);
    }
}
