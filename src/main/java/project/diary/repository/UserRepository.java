package project.diary.repository;

import project.diary.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByID(Long userId);

    List<User> findAll();

    public Optional<User> findByLoginId(String loginID);
}
