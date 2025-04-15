package project.diary.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.diary.domain.ShareRequest;
import project.diary.mapper.ShareRequestMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisShareRequestRepository implements ShareRequestRepository {

    private final ShareRequestMapper shareRequestMapper;

    //새로운 공유 요청 저장
    @Override
    public void saveFriendRequest(Long senderId, Long receiverId) {
        //기존에 거절했던 요청이 있는지 체크
        int rejectedCount = shareRequestMapper.checkRejectedCount(senderId, receiverId);
        if (rejectedCount > 0) {
            // 기존에 거절했던 공유 요청이 존재 -> 다시 PENDING으로 변경
            shareRequestMapper.updateRejectedToPending(senderId, receiverId);
        } else {
            // 신규 요청 저장
            shareRequestMapper.saveFriendRequest(senderId, receiverId);
        }
    }

    //대기중인 공유 요청이 존재하는지 확인
    @Override
    public boolean isRequestExists(Long senderId, Long receiverId) {
        return shareRequestMapper.isRequestExists(senderId, receiverId);
    }

    //유저가 받은 공유 요청 조회
    @Override
    public List<ShareRequest> findPendingRequestsToMe(Long receiverId) {
        return shareRequestMapper.findPendingRequestsToMe(receiverId);
    }

    //유저가 보낸 공유 요청 조회
    @Override
    public List<ShareRequest> findPendingRequestsFromMe(Long senderId) {
        return shareRequestMapper.findPendingRequestsFromMe(senderId);
    }

    //받은 공유 요청 수락
    @Override
    public void acceptRequest(Long requestId) {
        shareRequestMapper.acceptRequest(requestId);
    }

    //받은 공유 요청 거절
    @Override
    public void rejectRequest(Long requestId) {
        shareRequestMapper.rejectRequest(requestId);
    }
}
