package project.diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.diary.domain.User;
import project.diary.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findByID(userId);
    }

    public Optional<User> findByLoginId(String loginID) {
        return userRepository.findByLoginId(loginID);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


}
