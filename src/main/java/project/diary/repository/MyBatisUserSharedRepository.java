package project.diary.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.diary.domain.User;
import project.diary.mapper.UserSharedMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisUserSharedRepository implements UserSharedRepository {

    private final UserSharedMapper userSharedMapper;

    /**
     * 다이어리를 특정 사용자에게 공유함 (user_shared 테이블에 추가)
     * @param ownerUserId 로그인한 다이어리 소유자
     * @param sharedUserId 공유 대상자
     */
    @Override
    public void save(Long ownerUserId, Long sharedUserId) {
        userSharedMapper.save(ownerUserId, sharedUserId);
    }

    @Override
    public Optional<User> findUserIdByLoginId(String loginId) {
        return userSharedMapper.findUserIdByLoginId(loginId);
    }

    @Override
    public boolean isAlreadyShared(Long ownerUserId, Long sharedUserId) {
        return userSharedMapper.isAlreadyShared(ownerUserId, sharedUserId);
    }

    @Override
    public List<User> findSharedUsers(Long userId) {
        return userSharedMapper.findSharedUsers(userId);
    }

    @Override
    public List<User> findUsersWhoSharedWithMe(Long userId) {
        return userSharedMapper.findUsersWhoSharedWithMe(userId);
    }

    @Override
    public void delete(Long ownerUserId, Long sharedUserId) {
        userSharedMapper.delete(ownerUserId, sharedUserId);
    }
}
