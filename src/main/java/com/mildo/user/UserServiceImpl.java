package com.mildo.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void login(String name, String googleId, String email) {
        UserVO dummyUser = new UserVO("#W34B", name, googleId, email, "N", "N", 3452);
        log.info("[UserServiceImpl] dummyUser : {}", dummyUser);

        userRepository.signup(dummyUser);
    }
}
