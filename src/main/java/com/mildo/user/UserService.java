package com.mildo.user;


import com.mildo.code.CodeRepository;
import com.mildo.study.Vo.EnteStudy;
import com.mildo.user.Auth.JwtTokenProvider;
import com.mildo.user.Vo.BlackTokenVO;
import com.mildo.user.Vo.LevelCountDTO;
import com.mildo.user.Vo.TokenVO;
import com.mildo.user.Vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 클래스

    public UserVO login(OidcUser principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String number = (String) principal.getAttributes().get("sub");  // sub는 String 타입
        UserVO users = new UserVO();
        UserVO user = userRepository.findUser(number); // 회원 인지 조회

        if (user == null) { // 회원이 아니면 ID찾아서 회원 정보 업데이트 시켜줌
            String userId = userRepository.findNullUserId();
            users.setUserId(userId);
            users.setUserEmail(email);
            users.setUserName(name);
            users.setUserGoogleId(number);
            userRepository.saveUpdateUser(users);
            user = userRepository.findUser(number);
        }

        return user;
    }

    public UserVO finduserId(String userId) {
        return userRepository.finduserId(userId);
    }

    public TokenVO makeToken(String userId) { // 토큰 생성 관련 메서드
        TokenVO token = new TokenVO();
        UserVO user = userRepository.finduserId(userId); // 존재하는 User인지 검증
        if(user == null) {return token;} // user가 없으면 null 보냄

        TokenVO vo = userRepository.findToken(userId); // DB에 토큰 검증

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        Date expiration = jwtTokenProvider.getExpirationFromRefreshToken(refreshToken);
        // refreshToken 만료시간 형변환
        java.sql.Timestamp sqlExpiration = new java.sql.Timestamp(expiration.getTime());

        token.setUserId(userId);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpirationTime(sqlExpiration);

        if(vo == null){ // 토큰이 없으면 INSERT
            userRepository.saveToken(token);
        } else { // 토큰이 있으면 UPDATE
            userRepository.saveUpdateToken(token);
        }
        return token;
    }

    // 블랙리스트 초기화
    public void blackrest(Timestamp timestamp){
        userRepository.blackrest(timestamp);
    }

    // 필터에서 토큰 조회
    public String findRefreshTokenByUserId(String userId){
        return userRepository.findRefreshTokenByUserId(userId);
    }

    public List<LevelCountDTO> solvedLevelsList(String userId) {
        // 코드 레벨별로 갯수 가져오기
        List<LevelCountDTO> solvedLevels = userRepository.solvedLevelsList(userId);
        log.info("solvedLevels = {}", solvedLevels);

        return solvedLevels;
    }

    public void updateStudyId(EnteStudy enteStudy) {
        userRepository.updateStudyId(enteStudy);
    }

    public boolean checkExtensionSync(String userId, String studyId) {
        return userRepository.checkExtensionSync(userId, studyId);
    }

    @Transactional
    public int studyGetOut(String userId) {
        return userRepository.studyGetOut(userId);
    }

    public int userTotalSolved(String userId) {
        return codeRepository.totalSolved(userId);
    }

    public int serviceOut(String userId) {
        return userRepository.serviceOut(userId);
    }

    // 로그아웃
    public String blackToken(String userId) {
        TokenVO vo = userRepository.findToken(userId);

        if(vo.getAccessToken() == null) return "토큰이 없음";

        // 토큰이 유효한지도 확인해야겠네

        Date expiration = jwtTokenProvider.getExpirationFromToken(vo.getAccessToken());
        java.sql.Timestamp sqlExpiration = new java.sql.Timestamp(expiration.getTime());

        BlackTokenVO black = new BlackTokenVO();
        black.setBlackToken(vo.getAccessToken());
        black.setExpirationTime(sqlExpiration);
        userRepository.saveBlackToken(black); // 블랙리스트 추가

        userRepository.tokenNull(userId); // 토큰 비워 주기

        return "로그아웃 성공";
    }

    public int changUserInfo(String userId, UserVO vo){
        if(vo.getUserName() != null && vo.getUserTheme() != null){ // 이름 테마 둘다 변경
            return userRepository.changUserInfo(userId, vo);
        } else if(vo.getUserName() == null){ // 테마만 변경
            return userRepository.changUserTheme(userId, vo.getUserTheme());
        } else if(vo.getUserTheme() == null){ // 이름만 변경
            return userRepository.changUserName(userId, vo.getUserName());
        }
        return 0;
    }

}
