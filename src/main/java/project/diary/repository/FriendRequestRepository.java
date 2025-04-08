package project.diary.repository;

import project.diary.domain.FriendRequest;

import java.util.List;

public interface FriendRequestRepository {
    void saveFriendRequest(Long senderId, Long receiverId);
    boolean isRequestExists(Long senderId, Long receiverId);
    List<FriendRequest> findPendingRequestsToMe(Long receiverId);
    List<FriendRequest> findPendingRequestsFromMe(Long senderId);
    void acceptRequest(Long requestId);
    void rejectRequest(Long requestId);

}
