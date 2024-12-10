package com.mildo.study;

import com.mildo.study.Vo.EnteStudy;
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

    @ResponseBody
    @PostMapping(value="/{userId}/create", produces="application/json; charset=UTF-8")
    public Map<String, Object> create(@PathVariable String userId, @RequestBody StudyVO studyVO) {
        // 유저 아이디 검증 (존재하는 아이디인지 + 토큰 검증)
        Map<String, Object> response = new HashMap<>();
        UserVO userVO = userService.finduserId(userId);

        if(userVO.getStudyId() != null) { // 유저 studyId가 있으면 null 반납
            response.put("studyId", userVO.getStudyId());
            response.put("is_participainted", true);
            return response;
        }

        String studyId = studyService.create(userId, studyVO);
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
    public ResponseEntity<List<StudyVO>> ranks(@PathVariable String studyId) {
        List<StudyVO> totalrank = studyService.totalrank(studyId);
        return ResponseEntity.ok(totalrank);
    }

    //스터디 참가하기
    @ResponseBody
    @PutMapping(value="/enterStudy", produces="application/json; charset=UTF-8")
    public ResponseEntity<String> enterStudy(@RequestBody EnteStudy enteStudy) {
        int res = studyService.totalMembers(enteStudy.getStudyId());
        if(res >= 6){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인원 초과");
        }

        UserVO userVO = userService.finduserId(enteStudy.getUserId());
        if(userVO.getStudyId() != null){
            log.info("userVO.getStudyId() = {}", userVO.getStudyId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 스터디가 있습니다. Study ID: "+ userVO.getStudyId());
        }

        boolean isValid = studyService.checkstudyIdPassword(enteStudy); // 비번 체크
        if (isValid) {
            userService.updateStudyId(enteStudy);
            return ResponseEntity.ok("스터디 접속 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid study code or password!");
        }
    }

    @GetMapping("{studyId}/monthData")
    public List<Map<String, Object>> monthData(@PathVariable String studyId){
        return studyService.Mildo(studyId);
    }

    // 스터디 이름 바꾸기 구현
    @ResponseBody
    @PutMapping(value = "/{studyId}/rename", produces="application/json; charset=UTF-8")
    public ResponseEntity<String> updateUser(@PathVariable String studyId, @RequestBody StudyVO vo) {
        return studyService.updateStudyName(studyId, vo.getStudyName()) > 0 ? ResponseEntity.ok("변경 성공") : ResponseEntity.ok("변경 실패");
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
        int res = studyService.deleteStudyUser(studyId); // 회원 정보 수정, 코드, 댓글 다 삭제
        
        boolean isValid =  studyService.deleteStudy(studyId); // 스터디 삭제인 듯
        if (isValid) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        }

        return ResponseEntity.ok(true);
    }
}
