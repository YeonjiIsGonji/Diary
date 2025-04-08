package project.diary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.diary.domain.FriendRequest;
import project.diary.domain.User;
import project.diary.repository.DiaryRepository;
import project.diary.repository.FriendRequestRepository;
import project.diary.repository.UserSharedRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiarySharingService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserSharedRepository userSharedRepository;

    /**
     * 친구 요청 보내기
     */
    public String sendFriendRequest(Long senderId, String receiverLoginId) {
        Optional<User> sharedUser = userSharedRepository.findUserIdByLoginId(receiverLoginId);

        if (sharedUser.isEmpty()) {
            return "존재하지 않는 ID입니다.";
        }

        Long receiverId = sharedUser.get().getUserId();

        if (sharedUser.get().getUserId().equals(senderId)) {
            return "본인에게 초대 요청을 보낼 수 없습니다.";
        }

        if (userSharedRepository.isAlreadyShared(senderId, receiverId)) {
            return "이미 초대된 사용자입니다.";
        }

        if (friendRequestRepository.isRequestExists(senderId, receiverId)) {
            return "이미 초대 요청을 보냈습니다.";
        }

        friendRequestRepository.saveFriendRequest(senderId, receiverId);
        return "초대 요청이 전송되었습니다.";
    }

    /**
     * 받은 친구 요청 조회
     */
    public List<FriendRequest> getPendingRequestsToMe(Long receiverId) {
        return friendRequestRepository.findPendingRequestsToMe(receiverId);
    }

    /**
     * 보낸 친구 요청 조회
     */
    public List<FriendRequest> getPendingRequestsFromMe(Long senderId) {
        return friendRequestRepository.findPendingRequestsFromMe(senderId);
    }

    /**
     * 초대 요청 수락
     */
    public void acceptFriendRequest(Long friendRequestID, Long senderId, Long receiverId) {
        friendRequestRepository.acceptRequest(friendRequestID);
        userSharedRepository.save(senderId, receiverId);
    }

    /**
     * 초대 요청 거절
     */
    public void rejectFriendRequest(Long requestId) {
        friendRequestRepository.rejectRequest(requestId);
    }

//    public void shareDiary(Long ownerUserId, String sharedUserLoginId) {
//        Optional<User> sharedUser = userSharedRepository.findUserIdByLoginId(sharedUserLoginId);
//
//        if (sharedUser.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
//        }
//
//        if (sharedUser.get().getUserId().equals(ownerUserId)) {
//            throw new IllegalArgumentException("자기 자신을 친구 추가할 수 없습니다.");
//        }
//
//        Long sharedUserId = sharedUser.get().getUserId();
//
//        if (userSharedRepository.isAlreadyShared(ownerUserId, sharedUserId)) {
//            throw new IllegalArgumentException("이미 공유된 사용자입니다.");
//        }
//
//        userSharedRepository.save(ownerUserId, sharedUserId);
//    }

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
