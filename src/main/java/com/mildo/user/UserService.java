package com.mildo.user;


import com.mildo.code.CodeVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.common.Page.Pagenation;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.UserVO;
import com.mildo.utill.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserVO login(OidcUser principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String number = (String) principal.getAttributes().get("sub");  // sub는 String 타입

        UserVO users = new UserVO();
        String userId = CodeGenerator.UserId();
        // TODO userId가 무결성을 해치지 않는지 검증하는 코드 필요할 것으로 추정

        users.setUserId(userId); // #G909
        users.setUserEmail(email);
        users.setUserName(name);
        users.setUserGoogleId(number);

        UserVO user = userRepository.findUser(number);

        if(user == null){
            userRepository.save(users);
            user = userRepository.findUser(number);
        }

        return user;
    }

    public UserVO finduserId(String userId){
        return userRepository.finduserId(userId);
    }

    public List<LevelCountDTO> solvedLevelsList(String userId){
        // 코드 레벨별로 갯수 가져오기
        List<LevelCountDTO> solvedLevels = userRepository.solvedLevelsList(userId);
        log.info("solvedLevels = {}", solvedLevels);

        return solvedLevels;
    }

    public List<CodeVO> solvedList(String userId){
        // 문제 푼 총 수량
        int totalSolved = userRepository.totalSolved(userId);
        log.info("totalSolved = {}", totalSolved);

        // 리스트 페이지 별로 주는 메서드
        PageInfo pi = Pagenation.getPageInfo(totalSolved, 1, 5, 9);
        log.info("pi = {}", pi);

        // 문재 리스트
        List<CodeVO> solvedList = userRepository.solvedList(pi, userId);
        log.info("solvedList = {}", solvedList);

        return solvedList;
    }

}
