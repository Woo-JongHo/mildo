package com.mildo.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserVO login(OidcUser principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String number = (String) principal.getAttributes().get("sub");  // sub는 String 타입

        UserVO users = new UserVO();
        users.setUserId("#G909"); // #G909
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
}
