package com.mildo.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mildo.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
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
    @CrossOrigin(origins = "chrome-extension://ghbibjdmcondjdiebninoidgihdklndj")
    @PostMapping("/receive-sync")
    public ResponseEntity<String> receiveSync(@RequestBody String data) {
        try {

            /* 연동하기 눌렀을 때 ID, 와 STUDYCODE 확인 후, 연동하는 작업 */
            ObjectMapper sync = new ObjectMapper();
            JsonNode convertSync = sync.readTree(data);

            // 필요한 정보 추출
            String userId = convertSync.get("id").asText();
            String studyId = convertSync.get("studyCode").asText();

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


}
