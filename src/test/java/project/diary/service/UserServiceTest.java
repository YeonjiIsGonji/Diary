package project.diary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    private Long userId;

    @BeforeEach
    void init() {
        User user = new User("testUser1", "test1");
        User user2 = new User("testUser2", "test2");
        userService.save(user);
        userService.save(user2);

        userId = user.getUserId();
    }

    @Test
    void save() {
        User user = new User("userB", "userB");
        User saved = userService.save(user);
        assertThat(saved.getLoginId()).isEqualTo("userB");
        assertThat(saved.getPassword()).isEqualTo("userB");
    }

    @Test
    void findByLoginId() {
        Optional<User> result = userService.findByLoginId("testUser1");
        assertThat(result).isPresent();
        assertThat(result.get().getPassword()).isEqualTo("test1");
    }

    @Test
    void findAllUsers() {
        List<User> users = userService.findAllUsers();
        List<String> loginIds = users.stream().map(User::getLoginId).toList();

        assertThat(loginIds).contains("testUser1", "testUser2");
    }

    @Test
    void findUserById() {
        Optional<User> findUser = userService.findUserById(userId);
        assertThat(findUser.get().getLoginId()).isEqualTo("testUser1");
    }
}