package com.mildo.user;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface UserService {

    // 구글 로그인
    UserVO login(OidcUser principal);

}
