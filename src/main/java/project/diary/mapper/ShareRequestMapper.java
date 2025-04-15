package project.diary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.diary.domain.ShareRequest;

import java.util.List;

@Mapper
public interface ShareRequestMapper {
    //새로운 공유 요청 저장
    void saveFriendRequest(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    //거절된 요청을 다시 대기중(PENDING)으로 전환
    void updateRejectedToPending(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    //기존에 존재하는 거절된 요청 체크
    int checkRejectedCount(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    //현재 대기중(PENDING)인 공유 오청이 있는지 체크
    boolean isRequestExists(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    //유저가 받은 공유 요청 리스트
    List<ShareRequest> findPendingRequestsToMe(@Param("receiverId") Long receiverId);
    //유저가 보낸 공유 요청 리스트
    List<ShareRequest> findPendingRequestsFromMe(@Param("senderId") Long senderId);
    //공유 요청 수락
    void acceptRequest(@Param("requestId") Long requestId);
    //공유 요청 거절
    void rejectRequest(@Param("requestId") Long requestId);
}
