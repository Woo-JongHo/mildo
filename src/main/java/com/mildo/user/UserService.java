package com.mildo.user;


import com.mildo.code.CodeVO;
import com.mildo.common.Page.PageInfo;
import com.mildo.common.Page.Pagenation;
import com.mildo.user.Auth.JwtTokenProvider;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import com.mildo.utills.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.mildo.user.Auth.JwtTokenProvider.getExpirationFromToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 클래스

    public UserVO login(OidcUser principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String number = (String) principal.getAttributes().get("sub");  // sub는 String 타입

        UserVO users = new UserVO();

        // 유저 아이디가 무결성을 유지하는지 검증 필요
        String userId = CodeGenerator.generateUserId();
        while (finduserId(userId) != null)
            userId = CodeGenerator.generateUserId();
        users.setUserId(userId); // #G909

        users.setUserEmail(email);
        users.setUserName(name);
        users.setUserGoogleId(number);
        users.setUserTheme("#FFFFFF");
        UserVO user = userRepository.findUser(number);

        if (user == null) {
            userRepository.save(users);
            user = userRepository.findUser(number);
        }

        return user;
    }

    public UserVO finduserId(String userId) {
        return userRepository.finduserId(userId);
    }

    // DB토큰 일단 보류 논의 후에 정리
    public TokenVO saveToken(String userId) {

        TokenVO vo = userRepository.findToken(userId);

        if (vo == null) {
            String accessToken = jwtTokenProvider.createAccessToken(userId);
            Date expiration = jwtTokenProvider.getExpirationFromToken(accessToken);
            // 형변환
            java.sql.Timestamp sqlExpiration = new java.sql.Timestamp(expiration.getTime());

            TokenVO tkoen = new TokenVO();
            tkoen.setUserId(userId);
            tkoen.setAccessToken(accessToken);
            tkoen.setExpirationTime(sqlExpiration);

            userRepository.saveToken(tkoen);
            vo = userRepository.findToken(userId);
        }

        return vo;
    }

    public TokenVO makeToken(String userId) {
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        Date expiration = jwtTokenProvider.getExpirationFromToken(accessToken);
        // 형변환
        java.sql.Timestamp sqlExpiration = new java.sql.Timestamp(expiration.getTime());

        TokenVO tkoen = new TokenVO();
        tkoen.setUserId(userId);
        tkoen.setAccessToken(accessToken);
        tkoen.setExpirationTime(sqlExpiration);

        return tkoen;
    }

    public List<LevelCountDTO> solvedLevelsList(String userId) {
        // 코드 레벨별로 갯수 가져오기
        List<LevelCountDTO> solvedLevels = userRepository.solvedLevelsList(userId);
        log.info("solvedLevels = {}", solvedLevels);

        return solvedLevels;
    }

    public List<CodeVO> solvedList(String userId) {
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

    public void updateStudyId(String userId, String studyId) {
        userRepository.updateStudyId(userId, studyId);
    }

    public boolean checkExtensionSync(String userId, String studyId) {
        return userRepository.checkExtensionSync(userId, studyId);
    }
}
