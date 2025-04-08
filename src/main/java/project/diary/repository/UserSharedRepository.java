package project.diary.repository;

import project.diary.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserSharedRepository {

    void save(Long ownerUserId, Long sharedUserId);

    boolean isAlreadyShared(Long ownerUserId, Long sharedUserId);

    List<User> findSharedUsers(Long userId);

    List<User> findUsersWhoSharedWithMe(Long userId);

    Optional<User> findUserIdByLoginId(String loginId);

    public void delete(Long ownerUserId, Long sharedUserId);
}
