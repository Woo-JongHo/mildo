package com.mildo.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mildo.code.Vo.CodeVO;
import com.mildo.code.Vo.CommentVO;
import com.mildo.code.Vo.RecentVO;
import com.mildo.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController {

    private final CodeService codeService;
    private final UserService userService;

    @PostMapping("/dummyCode")
    public String login(String userId){
        log.info("userId = {}", userId);
        codeService.dummyCode(userId);

        return "Login Succeed!";
    }

    //연동하기 버튼으로 스터디와 아이디 확인
    // ResponseEntity로 서로 응답 상호작용
    @CrossOrigin(origins = "chrome-extension://kmleenknngfkjncchnbfenfamoighddf")
    @PostMapping("/receive-sync")
    public ResponseEntity<String> receiveSync(@RequestBody String data) {
        try {

            /* 연동하기 눌렀을 때 ID, 와 STUDYCODE 확인 후, 연동하는 작업 */
            ObjectMapper sync = new ObjectMapper();
            JsonNode convertSync = sync.readTree(data);

            // 필요한 정보 추출
            String userId = convertSync.get("id").asText();
            String studyId = convertSync.get("studyId").asText();

            System.out.println("익스텐션 동기화 확인 userId" + userId);
            System.out.println("익스텐션 동기화 확인 studyId" + studyId);

            // DB에 데이터가 있는지 확인
            boolean isValid = userService.checkExtensionSync(userId, studyId);

            if (isValid) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.ok("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing the request");
        }
    }

    @CrossOrigin(origins = {"chrome-extension://kmleenknngfkjncchnbfenfamoighddf", "https://school.programmers.co.kr"})
    @PostMapping("/receive-data")
    public ResponseEntity<String> receiveData(@RequestBody String data) {
        try {

            /* 파싱된 데이터를 옮기는 작업*/
            ObjectMapper Data = new ObjectMapper();
            JsonNode convertData = Data.readTree(data);

            // 필요한 정보 추출
            String userId = convertData.get("id").asText();
            String studyId = convertData.get("studyCode").asText();
            /*
             * sourceText - 답안
             * readme - 문제설명
             * filename - 문제명
             * commitMessage - 시간초 매모리.USERNO
             * */

            // DB에 데이터가 있는지 확인
            boolean isValid = userService.checkExtensionSync(userId, studyId);
            if (isValid) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.ok("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing the request");
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResponseEntity<String> upload(@RequestBody UploadDTO request) throws ParseException {

        codeService.codeUpload(request);
        return ResponseEntity.ok("Upload successful");
    }

    // userId로 푼 문제 리스트 조회 | 요청 방법:/api/%23G909/solvedList
    @ResponseBody
    @GetMapping(value="/{userId}/solvedList", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<CodeVO>> solvedListId(@PathVariable String userId,
                                                    @RequestParam(value="cpage", defaultValue="1") int currentPage){
        List<CodeVO> solvedList = codeService.solvedList(userId, currentPage);
        return ResponseEntity.ok(solvedList);
    }

    @ResponseBody // 상세 코드 페이지
    @GetMapping(value="/{userId}/{codeId}/getCodeByProblemId", produces="application/json; charset=UTF-8")
    public ResponseEntity<CodeVO> getCodeByProblemId(@PathVariable int codeId, @PathVariable String userId){
        log.info("userId = {}", userId);
        log.info("codeId = {}", codeId);
        return ResponseEntity.ok(codeService.detailCode(codeId, userId));
    }

    @ResponseBody
    @GetMapping(value="/{studyId}/getRecent", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<RecentVO>> recent(@PathVariable String studyId){
        List<RecentVO> list = codeService.getRecent(studyId);
        return ResponseEntity.ok(list);
    }

    @ResponseBody // 댓글 리스트
    @GetMapping(value="/{userId}/{codeId}/getCommentList", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<CommentVO>> getCommentList(@PathVariable int codeId, @PathVariable String userId,
                                                          @RequestParam(value="cpage", defaultValue="1") int currentPage){
        List<CommentVO> list = codeService.CommentList(codeId, currentPage);
        log.info("currentPage = {}", currentPage);
        return ResponseEntity.ok(list);
    }

    @ResponseBody // 댓글 작성
    @PostMapping(value="/{codeId}/comment", produces="application/json; charset=UTF-8")
    public void comment(@PathVariable int codeId, @RequestBody CommentVO comment){
        log.info("codeId = {}", codeId);
        log.info("comment = {}", comment);
        int res = codeService.saveComment(comment);
        // 어떻게 댓글 작성하면 다시 댓글 리스트 줘야 함 프론트랑 상의해서 가져옴
    }

    @ResponseBody // 댓글 수정
    @PutMapping(value="/{commentId}/commentUpdate", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<CommentVO>> commentUpdate(@PathVariable int commentId, @RequestBody CommentVO comment){
        List<CommentVO> res = codeService.updateComment(comment);
        return ResponseEntity.ok(res);
    }

    @ResponseBody // 댓글 삭제
    @DeleteMapping(value="/{commentId}/commentDelete", produces="application/json; charset=UTF-8")
    public ResponseEntity<List<CommentVO>> commentDelete(@PathVariable int commentId, @RequestBody CommentVO comment){
        List<CommentVO> res = codeService.deleteComment(commentId, comment);
        return ResponseEntity.ok(res);
    }
}
