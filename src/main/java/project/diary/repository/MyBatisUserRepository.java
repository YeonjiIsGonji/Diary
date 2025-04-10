package project.diary.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.diary.domain.User;
import project.diary.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisUserRepository implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        userMapper.save(user);
        return user;
    }

    @Override
    public Optional<User> findByLoginId(String loginID) {
        User user = userMapper.findByLoginId(loginID);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByID(Long userId) {
        User user = userMapper.findByUserId(userId);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
