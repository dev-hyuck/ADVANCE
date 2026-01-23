package com.example.advance;

import com.example.advance.common.entity.User;
import com.example.advance.common.enums.UserRoleEnum;
import com.example.advance.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostConstruct // 서버 실행시 제일 먼저 실행되는 어노테이션 (!)
    @Transactional
    public void init() {
        List<User> userList =
                List.of(
                new User("장지혁", passwordEncoder.encode("1234"), "user1@naver.com", UserRoleEnum.ADMIN),
                new User("박민수", passwordEncoder.encode("1234"), "user2@naver.com", UserRoleEnum.NORMAL)
                );
        for (User user : userList) {
            userService.save(user);
        }
    }
}
