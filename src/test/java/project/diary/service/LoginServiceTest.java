package project.diary.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.User;
import project.diary.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    private final String loginId = "testUser";
    private final String password = "test1234";

    @BeforeEach
    void init() {
        User user = new User(loginId, password);
        userRepository.save(user);
    }

    @Test
    void 로그인성공() {
        User result = loginService.login(loginId, password);
        assertThat(result).isNotNull();
        assertThat(result.getLoginId()).isEqualTo(loginId);
    }

    @Test
    void 로그인실패_아이디틀림() {
        User result = loginService.login("wrongId", password);
        assertThat(result).isNull();
    }

    @Test
    void 로그인실패_비밀번호틀림() {
        User result = loginService.login(loginId, "wrong");
        assertThat(result).isNull();
    }
}