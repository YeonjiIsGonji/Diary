package project.diary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.User;
import project.diary.repository.DiaryRepository;
import project.diary.repository.UserSharedRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiarySharingService {

    private final DiaryRepository diaryRepository;
    private final UserSharedRepository userSharedRepository;

    public void shareDiary(Long ownerUserId, String sharedUserLoginId) {
        Optional<User> sharedUser = userSharedRepository.findUserIdByLoginId(sharedUserLoginId);

        if (sharedUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (sharedUser.get().getUserId().equals(ownerUserId)) {
            throw new IllegalArgumentException("자기 자신을 친구 추가할 수 없습니다.");
        }

        Long sharedUserId = sharedUser.get().getUserId();

        if (userSharedRepository.isAlreadyShared(ownerUserId, sharedUserId)) {
            throw new IllegalArgumentException("이미 공유된 사용자입니다.");
        }

        userSharedRepository.save(ownerUserId, sharedUserId);
    }

    public List<User> getSharedUsers(Long userId) {
        return userSharedRepository.findSharedUsers(userId);
    }

    public List<User> findUsersWhoSharedWithMe(Long userId) {
        return userSharedRepository.findUsersWhoSharedWithMe(userId);
    }

    public void deleteSharedUser(Long ownerUserId, Long sharedUserId) {
        userSharedRepository.delete(ownerUserId, sharedUserId);
    }
}
