package project.diary.repository;

import project.diary.domain.ShareRequest;

import java.util.List;

public interface ShareRequestRepository {
    void saveFriendRequest(Long senderId, Long receiverId);
    boolean isRequestExists(Long senderId, Long receiverId);
    List<ShareRequest> findPendingRequestsToMe(Long receiverId);
    List<ShareRequest> findPendingRequestsFromMe(Long senderId);
    void acceptRequest(Long requestId);
    void rejectRequest(Long requestId);

}
